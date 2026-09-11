package se.sofievd.backend.outfit.explanation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendation")
public class OutfitExplanationController {
    private final OutfitExplanationService explanationService;

    public OutfitExplanationController(OutfitExplanationService explanationService) {
        this.explanationService = explanationService;
    }

    @PostMapping("/explanation")
    public ResponseEntity<?> explain(@RequestBody OutfitExplanationRequest request) {
        try {
            return ResponseEntity.ok(explanationService.explain(request));
        } catch (OutfitExplanationService.ProviderConfigurationException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("OpenRouter API key is not configured. Restart the backend after checking backend/.env.");
        } catch (OutfitExplanationService.ProviderRateLimitException exception) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("OpenRouter usage limit reached.");
        } catch (OutfitExplanationService.OpenRouterCreditsException exception) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body("OpenRouter needs more credits for this request.");
        } catch (OutfitExplanationService.ProviderApiException exception) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("OpenRouter rejected the explanation request with HTTP "
                    + exception.getStatusCode() + ".");
        } catch (OutfitExplanationService.ExplanationUnavailableException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Explanation failed in the backend: "
                    + exception.diagnosticMessage() + ".");
        }
    }
}