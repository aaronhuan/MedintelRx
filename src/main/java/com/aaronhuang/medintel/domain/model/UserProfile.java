package com.aaronhuang.medintel.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a user profile that owns medication intake history and related state.
 *
 * <p>Stores identity credentials plus a preferred time zone for converting local timestamps to UTC instants.</p>
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
     * Email address for the user.
     */
    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    /**
     * Password for the user (should be hashed before persistence).
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Column(nullable = false)
    private String password;

    /**
     * IANA time zone ID for the user (e.g., "America/New_York").
     */
    @JsonAlias("timezone")
    @NotBlank
    @Column(nullable = false)
    private String timeZoneId;

    /**
     * Optional phone number for the user.
     */
    private String phoneNumber;

    /**
     * Optional date of birth for the user.
     */
    private LocalDate dateOfBirth;

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
     * @param email the email address of the user
     * @param password the password for the user (should be hashed before persistence)
     */
    public UserProfile(String email, String password) {
        this(email, password, "UTC", null, null);
    }

    /**
     * Creates a user profile with an explicit time zone.
     *
     * @param email the email address of the user
     * @param password the password for the user (should be hashed before persistence)
     * @param timeZoneId the IANA time zone ID for the user
     */
    public UserProfile(String email, String password, String timeZoneId) {
        this(email, password, timeZoneId, null, null);
    }

    /**
     * Creates a user profile with optional phone number and date of birth.
     *
     * @param email the email address of the user
     * @param password the password for the user (should be hashed before persistence)
     * @param timeZoneId the IANA time zone ID for the user
     * @param phoneNumber the optional phone number
     * @param dateOfBirth the optional date of birth
     */
    public UserProfile(String email, String password, String timeZoneId, String phoneNumber, LocalDate dateOfBirth) {
        this.email = requireNonBlank(email, "email");
        this.password = requireNonBlank(password, "password");
        this.timeZoneId = (timeZoneId == null || timeZoneId.isBlank()) ? "UTC" : timeZoneId;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();   
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value;
    }

}
