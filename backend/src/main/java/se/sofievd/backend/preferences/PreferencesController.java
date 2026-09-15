package se.sofievd.backend.preferences;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.outfit.service.ClothingMapper;
import se.sofievd.backend.outfit.service.ClothingService;
import se.sofievd.backend.preferences.model.PreferencesRequest;
import se.sofievd.backend.preferences.model.PreferencesResponse;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {
    private final ClothingService clothingService;
    private final UserPreferencesRepository preferencesRepository;

    public PreferencesController(
            ClothingService clothingService,
            UserPreferencesRepository preferencesRepository
    ) {
        this.clothingService = clothingService;
        this.preferencesRepository = preferencesRepository;
    }

    @PostMapping
    public ResponseEntity<PreferencesResponse> savePreferences(
            @Valid @RequestBody PreferencesRequest preferences,
            Authentication authentication
    ) {
        String clerkUserId = authenticatedUserId(authentication);
        UserPreferences savedPreferences = preferencesRepository
                .findById(clerkUserId)
                .map(existing -> {
                    existing.updateFrom(preferences);
                    return existing;
                })
                .orElseGet(() -> new UserPreferences(clerkUserId, preferences));

        preferencesRepository.save(savedPreferences);

        PreferencesResponse response = new PreferencesResponse(
                savedPreferences.toRequest(),
                clothingService.getAllClothes().stream().map(ClothingMapper::toDto).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PreferencesRequest> getPreferences(Authentication authentication) {
        String clerkUserId = authenticatedUserId(authentication);
        return preferencesRepository.findById(clerkUserId)
                .map(preferences -> ResponseEntity.ok(preferences.toRequest()))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    private String authenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        return authentication.getName();
    }
}