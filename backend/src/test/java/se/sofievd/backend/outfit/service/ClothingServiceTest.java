package se.sofievd.backend.outfit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sofievd.backend.outfit.ClothingRepository;
import se.sofievd.backend.outfit.model.Clothing;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClothingServiceTest {

    @Mock
    private ClothingRepository clothingRepository;

    @InjectMocks
    private ClothingService clothingService;

    private Clothing shirt;
    private Clothing sweater;

    @BeforeEach
    public void setUp() {
        shirt = new Clothing();
        shirt.setName("Shirt");
        shirt.setCategory("TOP");
        shirt.setCloValue(0.09);

        sweater = new Clothing();
        sweater.setName("Sweater");
        sweater.setCategory("TOP");
        sweater.setCloValue(0.20);
    }

    @Test
    void getAllClothes_ReturnsListOfClothing() {
        when(clothingRepository.findAll()).thenReturn(List.of(shirt, sweater));

        List<Clothing> result = clothingService.getAllClothes();

        assertEquals(2, result.size());
        verify(clothingRepository).findAll();
    }

    @Test
    void findBestItemFromDb_MatchFoundInRange_ReturnsFirstMatch() {
        when(clothingRepository.findByCategoryAndCloValueBetween("TOP", 0.0, 0.26))
                .thenReturn(List.of(shirt));

        Clothing result = clothingService.findBestItemFromDb("TOP", 0.06);

        assertNotNull(result);
        assertEquals("Shirt", result.getName());
        verify(clothingRepository).findByCategoryAndCloValueBetween("TOP", 0.0, 0.26);
        verifyNoMoreInteractions(clothingRepository);
    }

    @Test
    void findBestItemFromDb_NoMatchInRange_FallsBackToAnyCategoryItem() {
        when(clothingRepository.findByCategoryAndCloValueBetween(eq("TOP"), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList())
                .thenReturn(List.of(sweater));

        Clothing result = clothingService.findBestItemFromDb("TOP", 0.30);

        assertNotNull(result);
        assertEquals("Sweater", result.getName());
        verify(clothingRepository, times(2))
                .findByCategoryAndCloValueBetween(eq("TOP"), anyDouble(), anyDouble());
    }

    @Test
    void findBestItemFromDb_NoItemsExistForCategory_ReturnsNull() {
        when(clothingRepository.findByCategoryAndCloValueBetween(eq("TOP"), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        Clothing result = clothingService.findBestItemFromDb("TOP", 0.3);

        assertNull(result);
    }

    @Test
    void findBestItemFromCategories_MatchFoundInFirstCategory_ReturnsMatch() {
        List<String> categories = List.of("TOP", "JACKET");

        when(clothingRepository.findByCategoryAndCloValueBetween("TOP", 0.0, 0.26))
                .thenReturn(List.of(shirt));

        Clothing result = clothingService.findBestItemFromCategories(categories, 0.06);

        assertNotNull(result);
        assertEquals("Shirt", result.getName());
        verify(clothingRepository, times(1)).findByCategoryAndCloValueBetween(anyString(), anyDouble(), anyDouble());
    }

    @Test
    void findBestItemFromCategories_NoMatchInRange_FallsBackToAnyItemInCategories() {
        List<String> categories = List.of("TOP", "JACKET");

        // First loop: no matches in target range [0.6, 1.0] for either category
        when(clothingRepository.findByCategoryAndCloValueBetween(eq("TOP"), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList())
                .thenReturn(List.of(shirt));
        when(clothingRepository.findByCategoryAndCloValueBetween(eq("JACKET"), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList());

        Clothing result = clothingService.findBestItemFromCategories(categories, 0.06);

        assertNotNull(result);
        assertEquals("Shirt", result.getName());
    }

    @Test
    void findBestItemFromCategories_NoItemsExistInAnyCategory_ReturnsNull() {
        List<String> categories = List.of("TOP", "JACKET");

        when(clothingRepository.findByCategoryAndCloValueBetween(anyString(), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        Clothing result = clothingService.findBestItemFromCategories(categories, 0.3);

        assertNull(result);
    }

    @Test
    void findByName_ReturnsMatchingClothing() {
        when(clothingRepository.findByName("Shirt")).thenReturn(shirt);

        Clothing result = clothingService.findByName("Shirt");

        assertNotNull(result);
        assertEquals("Shirt", result.getName());
        verify(clothingRepository).findByName("Shirt");
    }
}