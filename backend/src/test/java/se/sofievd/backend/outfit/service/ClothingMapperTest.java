package se.sofievd.backend.outfit.service;

import org.junit.jupiter.api.Test;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.Outfit;
import se.sofievd.backend.outfit.model.Recommendation;
import se.sofievd.backend.outfit.model.dto.ClothingDto;
import se.sofievd.backend.outfit.model.dto.OutfitResponse;
import se.sofievd.backend.outfit.model.dto.RecommendationResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClothingMapperTest {

    @Test
    public void toDto_ValidClothing_MapsAllFieldsCorrectly() {
        Clothing clothing = createClothing("Jacket", "OUTERWEAR", "http://example.com/jacket.png");

        ClothingDto result = ClothingMapper.toDto(clothing);

        assertEquals("Jacket", result.name());
        assertEquals("OUTERWEAR", result.category());
        assertEquals("http://example.com/jacket.png", result.imageUrl());
    }

    @Test
    public void toDto_NullClothing_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            ClothingMapper.toDto(null);
        });
    }

    @Test
    public void toOutFitResponse_ValidOutfit_MapsAllLayersCorrectly() {
        Clothing base = createClothing("T-Shirt", "BASE", "tshirt.png");
        Clothing bottom = createClothing("Jeans", "BOTTOM", "jeans.png");
        Clothing footwear = createClothing("Sneakers", "FOOTWEAR", "sneakers.png");
        Clothing socks = createClothing("Wool Socks", "SOCKS", "socks.png");
        Clothing mid = createClothing("Sweater", "MID", "sweater.png");
        Clothing outer = createClothing("Rain Coat", "OUTER", "coat.png");
        List<Clothing> accessories = List.of(createClothing("Hat", "ACCESSORY", "hat.png"));

        Outfit outfit = createOutfit(base, bottom, footwear, socks, mid, outer, accessories);

        OutfitResponse result = ClothingMapper.toOutFitResponse(outfit);

        assertEquals(base, result.base());
        assertEquals(bottom, result.bottom());
        assertEquals(footwear, result.footwear());
        assertEquals(socks, result.socks());
        assertEquals(mid, result.mid());
        assertEquals(outer, result.outer());
        assertEquals(accessories, result.accessories());
    }

    @Test
    public void toOutFitResponse_NullOutfit_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            ClothingMapper.toOutFitResponse(null);
        });
    }


    @Test
    public void toRecommendationResponse_ValidRecommendation_MapsWearNowAndPackInBag() {
        Clothing base = createClothing("T-Shirt", "BASE", "tshirt.png");
        Outfit wearNow = createOutfit(base, null, null, null, null, null, List.of());

        Clothing extraJacket = createClothing("Rain Jacket", "OUTER", "rain.png");
        List<Clothing> packInBag = List.of(extraJacket);

        Recommendation recommendation = createRecommendation(wearNow, packInBag);

        RecommendationResponse result = ClothingMapper.toRecommendationResponse(recommendation);

        assertNotNull(result.wearNow());
        assertEquals(base, result.wearNow().base());
        assertEquals(1, result.packInBag().size());
        assertEquals("Rain Jacket", result.packInBag().get(0).name());
    }

    @Test
    public void toRecommendationResponse_EmptyPackInBag_ReturnsEmptyList() {
        Outfit wearNow = createOutfit(null, null, null, null, null, null, List.of());
        Recommendation recommendation = createRecommendation(wearNow, List.of());

        RecommendationResponse result = ClothingMapper.toRecommendationResponse(recommendation);

        assertNotNull(result.packInBag());
        assertTrue(result.packInBag().isEmpty());
    }

    @Test
    public void toRecommendationResponse_NullRecommendation_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            ClothingMapper.toRecommendationResponse(null);
        });
    }

    @Test
    public void privateConstructor_Instantiation_InvokesConstructorForCoverage() throws Exception {
        java.lang.reflect.Constructor<ClothingMapper> constructor = ClothingMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ClothingMapper instance = constructor.newInstance();
        assertNotNull(instance);
    }

    private Clothing createClothing(String name, String category, String imageUrl) {
        Clothing clothing = new Clothing();
        clothing.setName(name);
        clothing.setCategory(category);
        clothing.setImageUrl(imageUrl);
        return clothing;
    }

    private Outfit createOutfit(Clothing base, Clothing bottom, Clothing footwear,
                                Clothing socks, Clothing mid, Clothing outer, List<Clothing> accessories) {
        Outfit outfit = new Outfit();
        outfit.setBase(base);
        outfit.setBottom(bottom);
        outfit.setFootwear(footwear);
        outfit.setSocks(socks);
        outfit.setMid(mid);
        outfit.setOuter(outer);
        outfit.setAccessories(accessories);
        return outfit;
    }

    private Recommendation createRecommendation(Outfit wearNow, List<Clothing> packInBag) {
        Recommendation rec = new Recommendation();
        rec.setWearNow(wearNow);
        rec.setPackInBag(packInBag);
        return rec;
    }
}