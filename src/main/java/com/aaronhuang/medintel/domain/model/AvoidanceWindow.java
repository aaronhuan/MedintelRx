package com.aaronhuang.medintel.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.aaronhuang.medintel.domain.model.enums.AvoidType;
import com.aaronhuang.medintel.domain.model.enums.Severity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a time window during which a target should be avoided.
 *
 * <p>Generated deterministically from interaction rules tied to an intake event.</p>
 */
@Getter
@Setter
@Entity
public class AvoidanceWindow {
    
    /**
     * Primary key for this avoidance window.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Category of the item to avoid (food or medication).
     */
    @Enumerated(EnumType.STRING)
    private AvoidType type; //ENUM for type of avoidance

    /**
     * Canonical key for the avoided target (food key or medication RxCUI).
     */
    private String avoidTargetKey; //key for food or medication to avoid

    /**
     * Severity level for the avoidance window.
     */
    @Enumerated(EnumType.STRING)
    private Severity severityLevel; //ENUM for severity level

    /**
     * Intake event that triggered this avoidance window.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intake_event_id", nullable = false)
    private IntakeEvent intakeEvent;

    /**
     * UTC instant when the window begins.
     */
    private Instant startTime;

    /**
     * UTC instant when the window ends.
     */
    private Instant endTime;

    /**
     * Human-readable explanation for why this window exists.
     */
    private String explanation;

    /**
     * Rule identifier that produced this avoidance window.
     */
    private String ruleId; //ID of the rule that triggered this avoidance window 

    /**
     * Default constructor for JPA.
     */
    protected AvoidanceWindow() {} //JPA default constructor

    /**
     * Creates a new avoidance window tied to an intake event.
     *
     * @param type category of item to avoid
     * @param avoidTargetKey canonical key of the avoided target
     * @param severityLevel severity level for the avoidance
     * @param intakeEvent intake event that triggered the window
     * @param startTime UTC instant when the window begins
     * @param endTime UTC instant when the window ends
     * @param explanation human-readable explanation
     * @param ruleId rule identifier that produced this window
     */
    public AvoidanceWindow(
        AvoidType type,
        String avoidTargetKey,
        Severity severityLevel,
        IntakeEvent intakeEvent,
        Instant startTime,
        Instant endTime,
        String explanation,
        String ruleId
    ) {
        this.type = type;
        this.avoidTargetKey = avoidTargetKey;
        this.severityLevel = severityLevel;
        this.intakeEvent = intakeEvent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.explanation = explanation;
        this.ruleId = ruleId;
    }

}




