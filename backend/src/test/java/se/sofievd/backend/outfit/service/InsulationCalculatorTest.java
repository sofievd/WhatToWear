package se.sofievd.backend.outfit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.sofievd.backend.outfit.model.enums.ActivityLevel;
import se.sofievd.backend.outfit.model.enums.ToleranceLevel;
import se.sofievd.backend.outfit.model.weather.CurrentWeatherState;
import se.sofievd.backend.weather.model.WeatherTimeSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InsulationCalculatorTest {

    private InsulationCalculator calculator;

    @BeforeEach
    public void setup(){
        calculator = new InsulationCalculator();
    }

    @Test
    public void calculateEffectiveTemperature_StandardInputs_ReturnsDifference(){
        double result = calculator.calculateEffectiveTemperature(20.0, 2.0);
        assertEquals(22.0, result);
    }

    @Test
    public void calculateEffectiveTemperature_ZeroTolerance_ReturnsSameTemperature(){
        double result = calculator.calculateEffectiveTemperature(20.0, 0.0);
        assertEquals(20.0, result);
    }

    @Test
    public void calculateEffectiveTemperature_NegativeTemperature_CalculatesCorrectly(){
        double result = calculator.calculateEffectiveTemperature(-20.0, 2.0);
        assertEquals(-18.0, result);
    }

    @Test
    public void calculateEffectiveTemperature_Decimal_ReturnsDifference(){
        double result = calculator.calculateEffectiveTemperature(21.5, 1.2);
        assertEquals(22.7, result);
    }

    @Test
    public void calculateEffectiveTemperature_NegativeTolerance_CalculatesCorrectly(){
        double result = calculator.calculateEffectiveTemperature(20.0, -4.0);
        assertEquals(16.0, result);
    }

    @Test
    public void calculateEffectiveTemperature_ToleranceExceedsTemperature_CalculatesCorrectly(){
        double result = calculator.calculateEffectiveTemperature(20.0, -30.0);
        assertEquals(-10.0, result);
    }

    @Test
    public void calculateEffectiveTemperature_ZeroTemperature_ReturnsSameTemperature(){
        double result = calculator.calculateEffectiveTemperature(0.0, 12.9);
        assertEquals(12.9, result);
    }

    @Test
    public void calculateEffectiveTemperature_ExtremeTemperature_ReturnsSameTemperature(){
        double result = calculator.calculateEffectiveTemperature(Double.MAX_VALUE, 0.0);
        assertEquals(Double.MAX_VALUE, result);
    }

    @Test
    public void calculateEffectiveTemperature_ExtremeTolerance_ReturnsSameTemperature(){
        double result = calculator.calculateEffectiveTemperature(0,  Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, result);
    }

    @Test
    public void calculateTargetClo_StandardCoolWeather_ReturnsCorrectValue() {
        // 10°C, Met 1.0, 10 km/h wind -> expected ~2.3496
        double result = calculator.calculateTargetClo(10.0, 1.0, 10.0);
        assertEquals(2.3496, result, 0.001);
    }

    @Test
    public void calculateTargetClo_ZeroWindSpeed_ReturnsCorrectValue() {
        // 10°C, Met 1.0, 0 km/h wind -> expected ~1.8496
        double result = calculator.calculateTargetClo(10.0, 1.0, 0.0);
        assertEquals(1.8496, result, 0.001);
    }

    @Test
    public void calculateTargetClo_SubZeroTemperature_ReturnsHighCloValue() {
        // -10°C requires high insulation (~4.5666)
        double result = calculator.calculateTargetClo(-10.0, 1.0, 10.0);
        assertEquals(4.5666, result, 0.001);
    }

    @Test
    public void calculateTargetClo_HotTemperature_ClampsToMinimumFloor() {
        // 35°C temperature would give negative Clo, should return min floor 0.15
        double result = calculator.calculateTargetClo(35.0, 1.0, 5.0);
        assertEquals(0.15, result, 0.0001);
    }

    @Test
    public void calculateTargetClo_HighMetValue_ClampsToMinimumFloor() {
        // High exertion (Met 8.0) reduces insulation need below 0.15, should return 0.15
        double result = calculator.calculateTargetClo(25.0, 8.0, 5.0);
        assertEquals(0.15, result, 0.0001);
    }

    @Test
    public void calculateTargetClo_WindSpeedAboveFive_AppliesMaxAirInsulationCap() {
        // Wind speeds >= 5 km/h cap the air insulation deduction at 0.5
        double resultAt5 = calculator.calculateTargetClo(10.0, 1.0, 5.0);
        double resultAt50 = calculator.calculateTargetClo(10.0, 1.0, 50.0);
        assertEquals(resultAt5, resultAt50, 0.0001);
    }

    @Test
    public void calculateTargetClo_ZeroMetValue_ReturnsPositiveInfinity() {
        // Division by zero (metValue * 58.2 = 0) returns Infinity
        double result = calculator.calculateTargetClo(10.0, 0.0, 5.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    }

    @Test
    public void analyzeCurrentWeather_NullTimeSlot_ReturnsZeroedState() {


        CurrentWeatherState result = calculator.analyzeCurrentWeather(null, ToleranceLevel.RUNS_HOT, ActivityLevel.SITTING);

        assertEquals(0.0, result.cloTarget(), 0.0001);
        assertEquals(0.0, result.windSpeed(), 0.0001);
        assertEquals(false, result.isSunny());
    }

    @Test
    public void analyzeCurrentWeather_NullTolerance_ThrowsNullPointerException() {
        WeatherTimeSlot slot = createTimeSlot(15.0, 10.0, true);

        assertThrows(NullPointerException.class, () -> {
            calculator.analyzeCurrentWeather(slot, null, ActivityLevel.WALKING);
        });
    }

    @Test
    public void analyzeCurrentWeather_NullActivity_ThrowsNullPointerException() {
        WeatherTimeSlot slot = createTimeSlot(15.0, 10.0, true);

        assertThrows(NullPointerException.class, () -> {
            calculator.analyzeCurrentWeather(slot, ToleranceLevel.NEUTRAL, null);
        });
    }

    @Test
    public void analyzeCurrentWeather_HotWeather_ClampsTargetCloToMinimumFloor() {
        // 35°C temperature triggers the 0.15 lower bound in calculateTargetClo
        WeatherTimeSlot slot = createTimeSlot(35.0, 5.0, true);


        CurrentWeatherState result = calculator.analyzeCurrentWeather(slot, ToleranceLevel.NEUTRAL, ActivityLevel.WALKING);

        assertEquals(0.15, result.cloTarget(), 0.0001);
    }



    private WeatherTimeSlot createTimeSlot(double minFeelsLike, double windSpeed, boolean isSunny) {
        WeatherTimeSlot slot = new WeatherTimeSlot();
        slot.setMinFeelsLikeTemperature(minFeelsLike);
        slot.setWindSpeed(windSpeed);
        slot.setSunny(isSunny);
        return slot;
    }
}