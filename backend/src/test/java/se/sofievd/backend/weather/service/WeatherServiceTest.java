package se.sofievd.backend.weather.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sofievd.backend.common.exception.LocationNotSupportedException;
import se.sofievd.backend.weather.model.Weather;
import se.sofievd.backend.weather.model.WeatherTimeSlot;
import se.sofievd.backend.weather.model.enums.TimeSlot;
import se.sofievd.backend.weather.openmeteo.OpenMeteoClient;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoCurrent;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoData;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoWeather;
import se.sofievd.backend.weather.smhi.SmhiAreaService;
import se.sofievd.backend.weather.smhi.SmhiClient;
import se.sofievd.backend.weather.smhi.model.SmhiData;
import se.sofievd.backend.weather.smhi.model.SmhiWeather;
import se.sofievd.backend.weather.smhi.model.TimeSerie;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private SmhiClient smhiClient;

    @Mock
    private SmhiAreaService areaService;

    @Mock
    private OpenMeteoClient openMeteoClient;

    @InjectMocks
    private WeatherService weatherService;


    @Test
    public void getGlobalWeather_ReturnsMappedWeather() {
        double lon = 18.06;
        double lat = 59.32;

        OpenMeteoCurrent current = mock(OpenMeteoCurrent.class);
        when(current.getTemperature()).thenReturn(15.0);
        when(current.getApparentTemperature()).thenReturn(14.0);
        when(current.getWindSpeed()).thenReturn(5.5);
        when(current.getRelativeHumidity()).thenReturn(65);
        when(current.getWeatherCode()).thenReturn(1); // WMO code
        when(current.getPrecipitation()).thenReturn(0.0);

        OpenMeteoWeather forecast = mock(OpenMeteoWeather.class);
        when(forecast.getCurrent()).thenReturn(current);

        when(openMeteoClient.getForecast(lon, lat)).thenReturn(forecast);

        Weather weather = weatherService.getGlobalWeather(lon, lat);

        assertNotNull(weather);
        assertEquals(15.0, weather.getAirTemperature());
        assertEquals(14.0, weather.getFeelsLikeTemperature());
        assertEquals(5.5, weather.getWindSpeed());
        assertEquals(65, weather.getHumidity());
        assertEquals(0.0, weather.getPrecipitation());
        assertNotNull(weather.getCondition());
        assertNotNull(weather.getTime());

        verify(openMeteoClient).getForecast(lon, lat);
    }

    @Test
    public void getAllDayWeather_Valid24HourData_ReturnsAllTimeSlots() {
        double lon = 18.06;
        double lat = 59.32;

        OpenMeteoData data = mock(OpenMeteoData.class);

        // Generate timestamps spanning all hours of the day (0 to 23)
        List<LocalDateTime> times = generate24HourTimestamps();
        List<Double> temps = Collections.nCopies(24, 15.0);
        List<Integer> humidities = Collections.nCopies(24, 60);
        List<Double> precip = Collections.nCopies(24, 0.0);
        List<Double> apparentTemps = Collections.nCopies(24, 14.0);
        List<Double> windSpeeds = Collections.nCopies(24, 3.0);
        List<Integer> codes = Collections.nCopies(24, 0);
        List<Double> uvIndices = Collections.nCopies(24, 1.0);

        when(data.getTime()).thenReturn(times);
        when(data.getTemperature()).thenReturn(temps);
        when(data.getRelativeHumidity()).thenReturn(humidities);
        when(data.getPrecipitation()).thenReturn(precip);
        when(data.getApparentTemperature()).thenReturn(apparentTemps);
        when(data.getWindSpeed()).thenReturn(windSpeeds);
        when(data.getWeatherCode()).thenReturn(codes);
        when(data.getUvIndex()).thenReturn(uvIndices);

        OpenMeteoWeather forecast = mock(OpenMeteoWeather.class);
        when(forecast.getHourly()).thenReturn(data);
        when(openMeteoClient.getForecast(lon, lat)).thenReturn(forecast);

        List<WeatherTimeSlot> result = weatherService.getAllDayWeather(lon, lat);

        assertNotNull(result);
        // Expect a slot for every value in TimeSlot enum
        assertEquals(TimeSlot.values().length, result.size());
        verify(openMeteoClient).getForecast(lon, lat);
    }

    @Test
    public void getAllDayWeather_MissingTimeSlotData_ThrowsIllegalArgumentException() {
        double lon = 18.06;
        double lat = 59.32;

        OpenMeteoData data = mock(OpenMeteoData.class);

        // Only provide 1 hour of data, which won't cover all required TimeSlot enum values
        LocalDateTime singleTime = LocalDateTime.of(2026, 9, 16, 10, 0);
        when(data.getTime()).thenReturn(List.of(singleTime));
        when(data.getTemperature()).thenReturn(List.of(15.0));
        when(data.getRelativeHumidity()).thenReturn(List.of(50));
        when(data.getPrecipitation()).thenReturn(List.of(0.0));
        when(data.getApparentTemperature()).thenReturn(List.of(14.0));
        when(data.getWindSpeed()).thenReturn(List.of(2.0));
        when(data.getWeatherCode()).thenReturn(List.of(1));
        when(data.getUvIndex()).thenReturn(List.of(0.5));

        OpenMeteoWeather forecast = mock(OpenMeteoWeather.class);
        when(forecast.getHourly()).thenReturn(data);
        when(openMeteoClient.getForecast(lon, lat)).thenReturn(forecast);

        assertThrows(IllegalArgumentException.class, () ->
                weatherService.getAllDayWeather(lon, lat)
        );
    }

    @Test
    public void getSmhiWeather_OutsideArea_ThrowsLocationNotSupportedException() {
        double lon = 5.0; // Outside Sweden / SMHI area
        double lat = 50.0;

        when(areaService.isInsideArea(lon, lat)).thenReturn(false);

        assertThrows(LocationNotSupportedException.class, () ->
                weatherService.getSmhiWeather(lon, lat)
        );

        verify(areaService).isInsideArea(lon, lat);
        verifyNoInteractions(smhiClient);
    }

    @Test
    public void getSmhiWeather_InsideArea_ReturnsWeather() {
        double lon = 18.06;
        double lat = 59.32;

        when(areaService.isInsideArea(lon, lat)).thenReturn(true);

        SmhiData mockData = mock(SmhiData.class);
        when(mockData.getAirTemperature()).thenReturn(15.0);
        when(mockData.getRelativeHumidity()).thenReturn(60);
        when(mockData.getWindSpeed()).thenReturn(3.5);
        when(mockData.getMaxPrecipitation()).thenReturn(0.0);
        when(mockData.getMinPrecipitation()).thenReturn(0.0);
        when(mockData.getSymbolCode()).thenReturn(1);

        TimeSerie mockTimeSerie = mock(TimeSerie.class);
        when(mockTimeSerie.getData()).thenReturn(mockData);

        SmhiWeather mockSmhiWeather = mock(SmhiWeather.class);
        when(mockSmhiWeather.getTimeSeries()).thenReturn(List.of(mockTimeSerie));

        when(smhiClient.getForecast(lon, lat)).thenReturn(mockSmhiWeather);

        Weather weather = weatherService.getSmhiWeather(lon, lat);

        assertNotNull(weather);
        assertEquals(15.0, weather.getAirTemperature());
        assertEquals(60, weather.getHumidity());
        verify(areaService).isInsideArea(lon, lat);
        verify(smhiClient).getForecast(lon, lat);
    }

    private List<LocalDateTime> generate24HourTimestamps() {
        LocalDateTime base = LocalDateTime.of(2026, 9, 16, 0, 0);
        LocalDateTime[] times = new LocalDateTime[24];
        for (int i = 0; i < 24; i++) {
            times[i] = base.plusHours(i);
        }
        return Arrays.asList(times);
    }
}