package se.sofievd.backend.preferences;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_preferences")
public class UserPreferences {
    @Id
    @Column(name = "clerk_user_id", nullable = false, length = 255)
    private String clerkUserId;

    @Column(nullable = false, length = 30)
    private String activity;

    @Column(name = "thermal_sensitivity", nullable = false, length = 30)
    private String thermalSensitivity;


    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserPreferences() {
    }

    public UserPreferences(String clerkUserId, PreferencesRequest request) {
        this.clerkUserId = clerkUserId;
        updateFrom(request);
    }

    public void updateFrom(PreferencesRequest request) {
        this.activity = request.activity();
        this.thermalSensitivity = request.thermalSensitivity();
    }

    @PrePersist
    @PreUpdate
    void updateTimestamp() {
        updatedAt = Instant.now();
    }

    public PreferencesRequest toRequest() {
        return new PreferencesRequest(activity, thermalSensitivity);
    }
}