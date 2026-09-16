package se.sofievd.backend.outfit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sofievd.backend.outfit.model.AccessoriesResult;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.Recommendation;
import se.sofievd.backend.outfit.model.enums.ActivityLevel;
import se.sofievd.backend.outfit.model.enums.ToleranceLevel;
import se.sofievd.backend.outfit.model.weather.CurrentWeatherState;
import se.sofievd.backend.outfit.model.weather.DailyWeatherMetrics;
import se.sofievd.backend.weather.model.WeatherTimeSlot;
import se.sofievd.backend.weather.model.enums.TimeSlot;
import se.sofievd.backend.weather.service.WeatherService;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private InsulationCalculator calculator;

    @Mock
    private ClothingRecommendationPolicy policy;

    @InjectMocks
    private RecommendationService recommendationService;


    @Test
    public void generateRecommendation_StandardFlow_ReturnsRecommendationWithWearNowAndBag() {

        List<WeatherTimeSlot> slots = createWeatherTimeSlotList();

        when(weatherService.getAllDayWeather(18.06, 59.32)).thenReturn(slots);

        CurrentWeatherState current = new CurrentWeatherState(1.2, 10.0, 8.0, 5.0, true);
        DailyWeatherMetrics daily = new DailyWeatherMetrics(15.0, 5.0, 0.0, 0.5, 1.5, true, false, false, false);

        when(calculator.analyzeCurrentWeather(any(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.WALKING)))
                .thenReturn(current);
        when(calculator.calculateDailyMetrics(anyList(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.WALKING)))
                .thenReturn(daily);

        Clothing sneakers = createClothing("sneakers", 0.1);
        Clothing socks = createClothing("socks", 0.05);
        Clothing trousers = createClothing("trousers", 0.3);
        Clothing shirt = createClothing("shirt", 0.2);

        when(policy.getFootwear(anyDouble(), anyDouble(), anyDouble())).thenReturn(sneakers);
        when(policy.getSocks(sneakers)).thenReturn(socks);
        when(policy.getBottom(anyDouble())).thenReturn(trousers);
        when(policy.getBaseLayer(anyDouble())).thenReturn(shirt);
        when(policy.getMidLayer(anyDouble())).thenReturn(createClothing("sweater", 0.4));
        when(policy.determineAccessories(any(), any()))
                .thenReturn(new AccessoriesResult(List.of(createClothing("sunglasses", 0.0)), List.of()));

        Recommendation result = recommendationService.generateRecommendation(
                18.06, 59.32, ToleranceLevel.NEUTRAL, ActivityLevel.WALKING
        );

        assertNotNull(result);
        assertNotNull(result.getWearNow());
        assertEquals(sneakers, result.getWearNow().getFootwear());
        assertEquals(trousers, result.getWearNow().getBottom());
        assertEquals(shirt, result.getWearNow().getBase());
    }

    @Test
    public void generateRecommendation_HighMetabolicActivity_JoggingOrRunning_PassesActivityToCalculator() {

        List<WeatherTimeSlot> slots = createWeatherTimeSlotList();
        when(weatherService.getAllDayWeather(anyDouble(), anyDouble())).thenReturn(slots);

        // Running produces lower clo targets from InsulationCalculator due to high body heat
        CurrentWeatherState current = new CurrentWeatherState(0.4, 10.0, 8.0, 5.0, false);
        DailyWeatherMetrics daily = new DailyWeatherMetrics(12.0, 8.0, 0.0, 0.3, 0.6, false, false, false, false);

        when(calculator.analyzeCurrentWeather(any(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.RUNNING)))
                .thenReturn(current);
        when(calculator.calculateDailyMetrics(anyList(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.RUNNING)))
                .thenReturn(daily);

        setupBasicPolicyMocks(0.2, 0.1);

        Recommendation result = recommendationService.generateRecommendation(
                18.06, 59.32, ToleranceLevel.NEUTRAL, ActivityLevel.RUNNING
        );

        assertNotNull(result);
        verify(calculator).analyzeCurrentWeather(any(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.RUNNING));
        verify(calculator).calculateDailyMetrics(anyList(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.RUNNING));
    }

    @Test
    public void generateRecommendation_LowMetabolicActivity_Sitting_PassesActivityToCalculator() {
        List<WeatherTimeSlot> slots = createWeatherTimeSlotList();
        when(weatherService.getAllDayWeather(anyDouble(), anyDouble())).thenReturn(slots);

        CurrentWeatherState current = new CurrentWeatherState(1.8, 10.0, 8.0, 5.0, false);
        DailyWeatherMetrics daily = new DailyWeatherMetrics(12.0, 8.0, 0.0, 1.0, 2.0, false, false, false, false);

        when(calculator.analyzeCurrentWeather(any(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.SITTING)))
                .thenReturn(current);
        when(calculator.calculateDailyMetrics(anyList(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.SITTING)))
                .thenReturn(daily);

        setupBasicPolicyMocks(0.3, 0.2);

        Recommendation result = recommendationService.generateRecommendation(
                18.06, 59.32, ToleranceLevel.NEUTRAL, ActivityLevel.SITTING
        );

        assertNotNull(result);
        verify(calculator).analyzeCurrentWeather(any(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.SITTING));
    }

    @Test
    public void generateRecommendation_BikingAndWalking_ExecutesWithoutError() {
        List<WeatherTimeSlot> slots =createWeatherTimeSlotList();
        when(weatherService.getAllDayWeather(anyDouble(), anyDouble())).thenReturn(slots);

        CurrentWeatherState current = new CurrentWeatherState(1.0, 15.0, 14.0, 10.0, true);
        DailyWeatherMetrics daily = new DailyWeatherMetrics(18.0, 12.0, 0.0, 0.4, 1.1, true, false, false, false);

        when(calculator.analyzeCurrentWeather(any(), any(), any())).thenReturn(current);
        when(calculator.calculateDailyMetrics(anyList(), any(), any())).thenReturn(daily);
        setupBasicPolicyMocks(0.3, 0.2);

        Recommendation bikingResult = recommendationService.generateRecommendation(
                18.06, 59.32, ToleranceLevel.NEUTRAL, ActivityLevel.BIKING
        );

        assertNotNull(bikingResult);
        verify(calculator).analyzeCurrentWeather(any(), eq(ToleranceLevel.NEUTRAL), eq(ActivityLevel.BIKING));
    }

    @Test
    public void generateRecommendation_RunsColdPreference_PassesToleranceToCalculator() {
        List<WeatherTimeSlot> slots = createWeatherTimeSlotList();
        when(weatherService.getAllDayWeather(anyDouble(), anyDouble())).thenReturn(slots);

        // RUNS_COLD leads higher insulation demands calculated
        CurrentWeatherState current = new CurrentWeatherState(1.5, 12.0, 10.0, 5.0, false);
        DailyWeatherMetrics daily = new DailyWeatherMetrics(15.0, 8.0, 0.0, 0.8, 1.8, false, true, false, false);

        when(calculator.analyzeCurrentWeather(any(), eq(ToleranceLevel.RUNS_COLD), eq(ActivityLevel.WALKING)))
                .thenReturn(current);
        when(calculator.calculateDailyMetrics(anyList(), eq(ToleranceLevel.RUNS_COLD), eq(ActivityLevel.WALKING)))
                .thenReturn(daily);

        setupBasicPolicyMocks(0.3, 0.2);

        Recommendation result = recommendationService.generateRecommendation(
                18.06, 59.32, ToleranceLevel.RUNS_COLD, ActivityLevel.WALKING
        );

        assertNotNull(result);
        verify(calculator).analyzeCurrentWeather(any(), eq(ToleranceLevel.RUNS_COLD), eq(ActivityLevel.WALKING));
    }

    @Test
    public void generateRecommendation_RunsHotPreference_PassesToleranceToCalculator() {
        List<WeatherTimeSlot> slots = createWeatherTimeSlotList();
        when(weatherService.getAllDayWeather(anyDouble(), anyDouble())).thenReturn(slots);

        CurrentWeatherState current = new CurrentWeatherState(0.5, 12.0, 10.0, 5.0, false);
        DailyWeatherMetrics daily = new DailyWeatherMetrics(15.0, 8.0, 0.0, 0.2, 0.7, false, false, false, false);

        when(calculator.analyzeCurrentWeather(any(), eq(ToleranceLevel.RUNS_HOT), eq(ActivityLevel.WALKING)))
                .thenReturn(current);
        when(calculator.calculateDailyMetrics(anyList(), eq(ToleranceLevel.RUNS_HOT), eq(ActivityLevel.WALKING)))
                .thenReturn(daily);

        setupBasicPolicyMocks(0.2, 0.1);

        Recommendation result = recommendationService.generateRecommendation(
                18.06, 59.32, ToleranceLevel.RUNS_HOT, ActivityLevel.WALKING
        );

        assertNotNull(result);
        verify(calculator).analyzeCurrentWeather(any(), eq(ToleranceLevel.RUNS_HOT), eq(ActivityLevel.WALKING));
    }

    @Test
    public void generateRecommendation_TargetCloExceeded_PacksExtraLayersInBag() {
        List<WeatherTimeSlot> slots = createWeatherTimeSlotList();
        when(weatherService.getAllDayWeather(anyDouble(), anyDouble())).thenReturn(slots);

        // Low target clo (0.3) -> base + bottom (0.5) already exceeds target clo
        CurrentWeatherState current = new CurrentWeatherState(0.3, 20.0, 20.0, 2.0, true);
        // High maxClo (1.2) triggers mid and outer layer selection
        DailyWeatherMetrics daily = new DailyWeatherMetrics(22.0, 15.0, 0.0, 0.3, 1.2, true, false, false, false);

        when(calculator.analyzeCurrentWeather(any(), any(), any())).thenReturn(current);
        when(calculator.calculateDailyMetrics(anyList(), any(), any())).thenReturn(daily);

        Clothing bottom = createClothing("trousers", 0.3);
        Clothing base = createClothing("shirt", 0.2);
        Clothing mid = createClothing("sweater", 0.4);

        when(policy.getFootwear(anyDouble(), anyDouble(), anyDouble())).thenReturn(createClothing("sneakers", 0.1));
        when(policy.getBottom(anyDouble())).thenReturn(bottom);
        when(policy.getBaseLayer(anyDouble())).thenReturn(base);
        when(policy.getMidLayer(anyDouble())).thenReturn(mid);
        when(policy.determineAccessories(any(), any())).thenReturn(new AccessoriesResult(List.of(), List.of()));

        Recommendation result = recommendationService.generateRecommendation(
                18.06, 59.32, ToleranceLevel.NEUTRAL, ActivityLevel.WALKING
        );

        // Sweater should be packed in bag, not worn, because base outfit (0.5) >= current clo target (0.3)
        assertNull(result.getWearNow().getMid());
        assertTrue(result.getPackInBag().contains(mid));
    }

    private Clothing createClothing(String name, double cloValue) {
        Clothing clothing = new Clothing();
        clothing.setName(name);
        clothing.setCloValue(cloValue);
        return clothing;
    }

    private void setupBasicPolicyMocks(double bottomClo, double baseClo) {
        Clothing footwear = createClothing("sneakers", 0.1);
        Clothing trousers = createClothing("trousers", bottomClo);
        Clothing shirt = createClothing("shirt", baseClo);

        lenient().when(policy.getFootwear(anyDouble(), anyDouble(), anyDouble())).thenReturn(footwear);
        lenient().when(policy.getSocks(any())).thenReturn(createClothing("socks", 0.05));
        lenient().when(policy.getBottom(anyDouble())).thenReturn(trousers);
        lenient().when(policy.getBaseLayer(anyDouble())).thenReturn(shirt);
        lenient().when(policy.determineAccessories(any(), any()))
                .thenReturn(new AccessoriesResult(List.of(), List.of()));
    }

    private void createTimeSlotsList(){
        LocalDateTime now = LocalDateTime.now();


        WeatherTimeSlot timeSlot = new WeatherTimeSlot();

        timeSlot.setTimeSlot(TimeSlot.MORNING);
    }

    private List<WeatherTimeSlot> createWeatherTimeSlotList() {
        LocalTime now = LocalTime.now();


        WeatherTimeSlot weatherTimeSlot = new WeatherTimeSlot();
        TimeSlot timeSlot = TimeSlot.from(now);
        weatherTimeSlot.setTimeSlot(timeSlot);


        return List.of(weatherTimeSlot);
    }

}