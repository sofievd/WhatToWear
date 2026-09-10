package se.sofievd.backend.outfit.explanation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import se.sofievd.backend.weather.openmeteo.OpenMeteoClient;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoData;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoWeather;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OutfitExplanationService {

    private static final Logger log =
            LoggerFactory.getLogger(OutfitExplanationService.class);

    private static final String MODEL = "openai/gpt-4o";

    private static final String SYSTEM_PROMPT = """
        Give one very short, natural explanation of why the recommended outfit fits today.

        Do not list the clothing items or repeat exact weather numbers.
        Keep it to one sentence, or two very short sentences only when useful.

        Only mention changes during the day when they matter, such as rain arriving later,
        a significant temperature change, or stronger wind. If the weather is straightforward,
        keep the explanation simple.

        Mention preferences only when they meaningfully affect comfort.
        Do not mention style preferences.

        Be concise, natural, and slightly friendly. A small emoji is okay.

        Return JSON only with exactly this string field:
        explanation.
        """;
    private final OpenMeteoClient openMeteoClient;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public OutfitExplanationService(
            OpenMeteoClient openMeteoClient,
            @Value("${openrouter.api-key:}") String apiKey
    ) {
        this.openMeteoClient = openMeteoClient;
        this.apiKey = apiKey;

        /*
         * LocalDateTime values are converted to strings in hourlyWeather(),
         * so no JavaTimeModule is required here.
         */
        this.objectMapper = new ObjectMapper();

        this.restClient = RestClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .build();
    }

    public OutfitExplanationResponse explain(OutfitExplanationRequest request) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new ProviderConfigurationException();
        }

        OpenMeteoWeather forecast = openMeteoClient.getForecast(
                request.longitude(),
                request.latitude()
        );

        try {
            String response = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("HTTP-Referer", "http://localhost:5173")
                    .header("X-Title", "WhatToWear")
                    .body(Map.of(
                            "model", MODEL,
                            "temperature", 0.2,
                            "max_tokens", 250,
                            "response_format", Map.of(
                                    "type", "json_object"
                            ),
                            "messages", List.of(
                                    Map.of(
                                            "role", "system",
                                            "content", SYSTEM_PROMPT
                                    ),
                                    Map.of(
                                            "role", "user",
                                            "content", buildContext(
                                                    request,
                                                    forecast
                                            )
                                    )
                            )
                    ))
                    .retrieve()
                    .body(String.class);

            JsonNode content = objectMapper
                    .readTree(response)
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content");

            if (!content.isTextual()) {
                throw new ExplanationUnavailableException();
            }

            JsonNode explanation =
                    objectMapper.readTree(content.asText());

            return new OutfitExplanationResponse(
                    requiredField(explanation, "explanation")
            );

        } catch (ProviderRateLimitException exception) {
            throw exception;

        } catch (RestClientResponseException exception) {

            if (exception.getStatusCode().value() == 429) {
                throw new ProviderRateLimitException();
            }

            if (exception.getStatusCode().value() == 402) {
                throw new OpenRouterCreditsException();
            }

            throw new ProviderApiException(
                    exception.getStatusCode().value()
            );

        } catch (
                RestClientException |
                ExplanationResponseException exception
        ) {

            log.warn(
                    "OpenRouter explanation failed: {}",
                    exception.getClass().getSimpleName(),
                    exception
            );

            throw new ExplanationUnavailableException(exception);

        } catch (Exception exception) {

            log.warn(
                    "Explanation preparation failed: {}",
                    exception.getClass().getSimpleName(),
                    exception
            );

            throw new ExplanationUnavailableException(exception);
        }
    }

    private String buildContext(
            OutfitExplanationRequest request,
            OpenMeteoWeather forecast
    ) {
        Map<String, Object> context = new LinkedHashMap<>();

        context.put(
                "recommendation",
                request.recommendation()
        );

        context.put(
                "preferences",
                request.preferences()
        );

        context.put(
                "hourlyWeather",
                hourlyWeather(forecast.getHourly())
        );

        return writeJson(context);
    }

    private List<Map<String, Object>> hourlyWeather(
            OpenMeteoData hourly
    ) {
        List<Map<String, Object>> entries = new ArrayList<>();

        if (hourly == null ||
                hourly.getTime() == null ||
                hourly.getTime().isEmpty()) {
            return entries;
        }

        LocalDate forecastDate =
                hourly.getTime().get(0).toLocalDate();

        for (int index = 0;
             index < hourly.getTime().size();
             index++) {

            if (!hourly.getTime()
                    .get(index)
                    .toLocalDate()
                    .equals(forecastDate)) {
                continue;
            }

            Map<String, Object> entry =
                    new LinkedHashMap<>();

            /*
             * Convert LocalDateTime to String.
             *
             * This avoids requiring Jackson's JavaTimeModule.
             *
             * Example:
             * 2026-09-10T14:00
             */
            entry.put(
                    "time",
                    hourly.getTime().get(index).toString()
            );

            entry.put(
                    "temperature",
                    valueAt(
                            hourly.getTemperature(),
                            index
                    )
            );

            entry.put(
                    "feelsLike",
                    valueAt(
                            hourly.getApparentTemperature(),
                            index
                    )
            );

            entry.put(
                    "windSpeed",
                    valueAt(
                            hourly.getWindSpeed(),
                            index
                    )
            );

            entry.put(
                    "precipitation",
                    valueAt(
                            hourly.getPrecipitation(),
                            index
                    )
            );

            entries.add(entry);
        }

        return entries;
    }

    private Object valueAt(
            List<?> values,
            int index
    ) {
        return values != null &&
                index >= 0 &&
                index < values.size()
                ? values.get(index)
                : null;
    }

    private String requiredField(
            JsonNode node,
            String field
    ) {
        JsonNode value = node.get(field);

        if (value == null ||
                !value.isTextual() ||
                value.asText().isBlank()) {

            throw new ExplanationResponseException(
                    "Missing or empty field: " + field
            );
        }

        return value.asText();
    }

    private String optionalField(
            JsonNode node,
            String field
    ) {
        JsonNode value = node.get(field);

        if (value == null ||
                !value.isTextual()) {

            throw new ExplanationResponseException(
                    "Missing or non-text field: " + field
            );
        }

        return value.asText();
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);

        } catch (Exception exception) {

            throw new ExplanationResponseException(
                    exception
            );
        }
    }

    private static class ExplanationResponseException
            extends RuntimeException {

        ExplanationResponseException(String message) {
            super(message);
        }

        ExplanationResponseException(Throwable cause) {
            super(cause);
        }
    }

    public static class ExplanationUnavailableException
            extends RuntimeException {

        public ExplanationUnavailableException() {
        }

        ExplanationUnavailableException(Throwable cause) {
            super(cause);
        }

        public String diagnosticMessage() {
            Throwable cause = getCause();

            return cause == null
                    ? (
                    getMessage() == null
                            ? "unknown error"
                            : getMessage()
            )
                    : (
                    cause.getMessage() == null
                            ? cause.getClass().getSimpleName()
                            : cause.getMessage()
            );
        }
    }

    public static class ProviderConfigurationException
            extends RuntimeException {
    }

    public static class ProviderRateLimitException
            extends RuntimeException {
    }

    public static class ProviderApiException
            extends RuntimeException {

        private final int statusCode;

        ProviderApiException(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    public static class OpenRouterCreditsException
            extends RuntimeException {
    }
}