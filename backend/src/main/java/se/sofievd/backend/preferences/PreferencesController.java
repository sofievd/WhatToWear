package se.sofievd.backend.preferences;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.outfit.ClothingMapper;
import se.sofievd.backend.outfit.ClothingService;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {
    private final ClothingService clothingService;

    public PreferencesController(ClothingService clothingService) {
        this.clothingService = clothingService;
    }

    @PostMapping
    public ResponseEntity<PreferencesResponse> savePreferences(
            @Valid @RequestBody PreferencesRequest preferences
    ) {
        PreferencesResponse response = new PreferencesResponse(
                preferences,
                clothingService.getAllClothes().stream().map(ClothingMapper::toDto).toList()
        );

        return ResponseEntity.ok(response);
    }
}