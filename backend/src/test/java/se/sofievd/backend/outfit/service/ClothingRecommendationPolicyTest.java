package se.sofievd.backend.outfit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sofievd.backend.outfit.model.AccessoriesResult;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.weather.CurrentWeatherState;
import se.sofievd.backend.outfit.model.weather.DailyWeatherMetrics;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClothingRecommendationPolicyTest {

    @Mock
    private ClothingService clothingService;

    @InjectMocks
    private ClothingRecommendationPolicy policy;

    @Test
    public void getOuterLayer_RainPrecipitation_ReturnsRainJacket() {
        Clothing rainJacket = createClothing("rain jacket");
        when(clothingService.findByName("rain jacket")).thenReturn(rainJacket);

        Clothing result = policy.getOuterLayer(1.2, 5.0); // 5.0mm precipitation

        assertEquals(rainJacket, result);
        verify(clothingService).findByName("rain jacket");
    }

    @Test
    public void getOuterLayer_RainPrecipitationButRainJacketNull_FallsBackToCategories() {
        Clothing defaultJacket = createClothing("heavy jacket");
        when(clothingService.findByName("rain jacket")).thenReturn(null);
        when(clothingService.findBestItemFromCategories(List.of("jackets", "outerwear"), 1.2))
                .thenReturn(defaultJacket);

        Clothing result = policy.getOuterLayer(1.2, 5.0);

        assertEquals(defaultJacket, result);
    }

    @Test
    public void getOuterLayer_NoPrecipitation_ReturnsCategoryBestItem() {
        Clothing jacket = createClothing("windbreaker");
        when(clothingService.findBestItemFromCategories(List.of("jackets", "outerwear"), 0.8))
                .thenReturn(jacket);

        Clothing result = policy.getOuterLayer(0.8, 0.0);

        assertEquals(jacket, result);
        verify(clothingService, never()).findByName("rain jacket");
    }

    @Test
    public void getFootwear_RainAndColdTemp_ReturnsBoots() {
        Clothing boots = createClothing("boots");
        when(clothingService.findByName("boots")).thenReturn(boots);

        // Precipitation > 0 and minTemp (5.0) < 6
        Clothing result = policy.getFootwear(10.0, 5.0, 2.0);

        assertEquals(boots, result);
        verify(clothingService).findByName("boots");
    }

    @Test
    public void getFootwear_RainAndMildTemp_ReturnsRainboots() {
        Clothing rainboots = createClothing("rainboots");
        when(clothingService.findByName("rainboots")).thenReturn(rainboots);

        // Precipitation > 0 and minTemp (10.0) >= 6
        Clothing result = policy.getFootwear(15.0, 10.0, 2.0);

        assertEquals(rainboots, result);
        verify(clothingService).findByName("rainboots");
    }

    @Test
    public void getFootwear_DryHotWeather_ReturnsSandals() {
        Clothing sandals = createClothing("sandals");
        when(clothingService.findByName("sandals")).thenReturn(sandals);

        // maxTemp > 22 and minTemp >= 16 with no rain
        Clothing result = policy.getFootwear(25.0, 18.0, 0.0);

        assertEquals(sandals, result);
    }

    @Test
    public void getFootwear_DryModerateWeather_ReturnsSneakers() {
        Clothing sneakers = createClothing("sneakers");
        when(clothingService.findByName("sneakers")).thenReturn(sneakers);

        Clothing result = policy.getFootwear(20.0, 12.0, 0.0);

        assertEquals(sneakers, result);
    }

    @Test
    public void getSocks_WearingSandals_ReturnsNull() {
        Clothing sandals = createClothing("sandals");

        Clothing result = policy.getSocks(sandals);

        assertNull(result);
        verifyNoInteractions(clothingService);
    }

    @Test
    public void getSocks_WearingSneakers_ReturnsSocks() {
        Clothing sneakers = createClothing("sneakers");
        Clothing socks = createClothing("socks");
        when(clothingService.findByName("socks")).thenReturn(socks);

        Clothing result = policy.getSocks(sneakers);

        assertEquals(socks, result);
    }

    @Test
    public void getBottom_HotTemperature_ReturnsShorts() {
        Clothing shorts = createClothing("shorts");
        when(clothingService.findByName("shorts")).thenReturn(shorts);

        assertEquals(shorts, policy.getBottom(25.0));
    }

    @Test
    public void getBottom_ModerateTemperature_ReturnsTrousers() {
        Clothing trousers = createClothing("trousers");
        when(clothingService.findByName("trousers")).thenReturn(trousers);

        assertEquals(trousers, policy.getBottom(15.0));
    }

    @Test
    public void getBottom_ColdTemperature_ReturnsFlannelTrousers() {
        Clothing flannel = createClothing("flannel trousers");
        when(clothingService.findByName("flannel trousers")).thenReturn(flannel);

        assertEquals(flannel, policy.getBottom(5.0));
    }

    @Test
    public void getBaseAndMidLayer_DelegatesToClothingService() {
        Clothing shirt = createClothing("shirt");
        Clothing sweater = createClothing("sweater");

        when(clothingService.findBestItemFromDb("shirts", 0.5)).thenReturn(shirt);
        when(clothingService.findBestItemFromDb("sweaters", 1.0)).thenReturn(sweater);

        assertEquals(shirt, policy.getBaseLayer(0.5));
        assertEquals(sweater, policy.getMidLayer(1.0));
    }

    @Test
    public void determineAccessories_NeedSunglassesAndSunny_WearsSunglassesNow() {
        Clothing sunglasses = createClothing("sunglasses");
        when(clothingService.findByName("sunglasses")).thenReturn(sunglasses);

        CurrentWeatherState current = new CurrentWeatherState(1.0, 15.0, 15.0, 5.0, true); // isSunny = true
        DailyWeatherMetrics daily = new DailyWeatherMetrics(23.0, 26.7,0.0,1.0, 1.3,true, false, false, false);   // sunglasses = true

        AccessoriesResult result = policy.determineAccessories(current, daily);

        assertTrue(result.worn().contains(sunglasses));
        assertFalse(result.packed().contains(sunglasses));
    }

    @Test
    public void determineAccessories_NeedSunglassesButNotSunny_PacksSunglassesInBag() {
        Clothing sunglasses = createClothing("sunglasses");
        when(clothingService.findByName("sunglasses")).thenReturn(sunglasses);

        CurrentWeatherState current = new CurrentWeatherState(1.0, 15.0, 15.0, 5.0, false); // isSunny = false
        DailyWeatherMetrics daily = new DailyWeatherMetrics(23.0, 26.7,0.0,1.0, 1.3, true, false, false, false);    // sunglasses = true

        AccessoriesResult result = policy.determineAccessories(current, daily);

        assertFalse(result.worn().contains(sunglasses));
        assertTrue(result.packed().contains(sunglasses));
    }

    @Test
    public void determineAccessories_NeedScarfWindy_WearsScarfNow() {
        Clothing scarf = createClothing("scarf");
        when(clothingService.findByName("scarf")).thenReturn(scarf);

        // Temp 10°C (< 12) and Wind 25 km/h (> 20) -> wearNow = true
        CurrentWeatherState current = new CurrentWeatherState(1.0, 10.0, 10.0, 25.0, false);
        DailyWeatherMetrics daily = new DailyWeatherMetrics(5.0, 6.7,0.0,1.0, 1.3,false, false, false, true); // needScarf = true

        AccessoriesResult result = policy.determineAccessories(current, daily);

        assertTrue(result.worn().contains(scarf));
    }

    private Clothing createClothing(String name) {
        Clothing clothing = new Clothing();
        clothing.setName(name);
        return clothing;
    }

}