package com.aaronhuang.medintel.domain.interaction;

import java.time.Instant;

import com.aaronhuang.medintel.domain.model.Medication;

/**
 * Represents a request to evaluate an intake as if it happened.
 *
 * <p>This separates preview intent from persisted {@link com.aaronhuang.medintel.domain.model.IntakeEvent}
 * so callers can evaluate without accidentally saving a simulated event.</p>
 */
public final class IntakeEvaluationRequest {
    private final Medication medication;
    private final Instant proposedTime;

    /**
     * Creates a request using the current time as the proposed intake time.
     *
     * @param medication medication to evaluate
     */
    public IntakeEvaluationRequest(Medication medication) {
        this(medication, Instant.now());
    }

    /**
     * Creates a request with an explicit proposed intake time.
     *
     * @param medication medication to evaluate
     * @param proposedTime proposed time of intake, defaults to now when null
     */
    public IntakeEvaluationRequest(Medication medication, Instant proposedTime) {
        this.medication = medication;
        this.proposedTime = (proposedTime == null) ? Instant.now() : proposedTime;
    }

    public Medication getMedication() {
        return medication;
    }

    public Instant getProposedTime() {
        return proposedTime;
    }
}
