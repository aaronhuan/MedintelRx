package com.aaronhuang.medintel.domain.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a medication saved by a specific user.
 *
 * <p>Links a user profile to a normalized medication and stores user-specific
 * regimen details without changing the canonical RxNorm record.</p>
 */
@Getter
@Setter
@Entity
public class UserMedication {
    /**
     * Primary key for this user medication record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Owning user profile for this medication.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile user;

    /**
     * Normalized medication referenced by RxCUI.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    /**
     * Default dosage description (e.g., "10mg").
     */
    private String dosage;

    /**
     * Free-text frequency or schedule (e.g., "BID", "every 8 hours").
     */
    private String frequency;

    /**
     * UTC instant when the user started this medication.
     */
    private Instant startDate;

    /**
     * UTC instant when the user stopped this medication.
     */
    private Instant endDate;

    /**
     * Optional notes entered by the user.
     */
    private String notes;

    /**
     * Whether this medication is taken as needed (PRN).
     */
    private boolean asNeeded;

    /**
     * Whether the medication is currently active for the user.
     */
    private boolean active;

    /**
     * UTC timestamp when this record was created.
     */
    private Instant createdAt;

    /**
     * Default constructor for JPA.
     */
    protected UserMedication() {}

    /**
     * Creates a user medication record with defaults.
     *
     * @param user owning user profile
     * @param medication normalized medication reference
     * @param dosage default dosage description
     */
    public UserMedication(UserProfile user, Medication medication, String dosage) {
        this.user = user;
        this.medication = medication;
        this.dosage = dosage;
        this.active = true;
        this.asNeeded = false;
        this.createdAt = Instant.now();
    }
}
