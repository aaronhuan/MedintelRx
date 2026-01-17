package com.aaronhuang.medintel.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user profile that owns medication intake history and related state.
 *
 * <p>Stores a preferred time zone for converting local timestamps to UTC instants.</p>
 */
@Getter //Lombok annotation to auto-generate getter methods for all fields
@Setter //Lombok annotation to auto-generate setter methods for all fields
@Entity
public class UserProfile {
    /**
     * Primary key for the user profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //generate unique IDs automatically
    private UUID id;

    /**
     * Display name of the user.
     */
    private String name;

    /**
     * IANA time zone ID for the user (e.g., "America/New_York").
     */
    private String timeZoneId;

    /**
     * UTC timestamp when this profile was created.
     */
    private Instant createdAt;
    
    /**
     * UTC timestamp when this profile was last updated.
     */
    private Instant updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected UserProfile(){} 

    /**
     * Creates a user profile with a default time zone of UTC.
     *
     * @param name the display name of the user
     */
    public UserProfile(String name) {
        this(name, "UTC");
    }

    /**
     * Creates a user profile with an explicit time zone.
     *
     * @param name the display name of the user
     * @param timeZoneId the IANA time zone ID for the user
     */
    public UserProfile(String name, String timeZoneId) {
        this.name = name;
        this.timeZoneId = (timeZoneId == null || timeZoneId.isBlank()) ? "UTC" : timeZoneId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();   
    }

}
