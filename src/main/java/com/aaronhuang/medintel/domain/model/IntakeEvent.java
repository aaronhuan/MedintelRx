package com.aaronhuang.medintel.domain.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a medication intake event for a user.
 *
 * <p>Intake times are stored as UTC instants for deterministic evaluation.</p>
 */
@Getter
@Setter
@Entity
public class IntakeEvent {
    /**
     * Primary key for this intake event.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Owning user profile for this intake event.
     */
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile user; //many intake events can belong to one user

    /**
     * Medication taken in this intake event.
     */
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication; //many intake events can belong to one medication

    /**
     * UTC instant when the intake occurred.
     */
    private Instant intakeTime;

    /**
     * Free-text dosage description (e.g., "10mg").
     */
    private String dosage;

    /**
     * UTC timestamp when the intake event was created.
     */
    private Instant createdAt;

    /**
     * Default constructor for JPA.
     */
    protected IntakeEvent() {} //JPA default constructor

    /**
     * Creates a new intake event.
     *
     * @param user owning user profile
     * @param medication medication taken
     * @param intakeTime UTC instant when the intake occurred
     * @param dosage free-text dosage description
     */
    public IntakeEvent(
        UserProfile user,
        Medication medication,
        Instant intakeTime,
        String dosage
    ) {
        this.user = user;
        this.medication = medication;
        this.intakeTime = intakeTime;
        this.dosage = dosage;
        this.createdAt = Instant.now();
    }

    /**
     * Creates a non-persisted intake event for preview evaluation.
     *
     * @param user owning user profile
     * @param medication medication to evaluate
     * @param intakeTime proposed intake time, defaults to now when null
     * @return simulated intake event (do not persist)
     */
    public static IntakeEvent simulated(
        UserProfile user,
        Medication medication,
        Instant intakeTime
    ) {
        Instant effectiveTime = (intakeTime == null) ? Instant.now() : intakeTime;
        return new IntakeEvent(user, medication, effectiveTime, null);
    }

}
