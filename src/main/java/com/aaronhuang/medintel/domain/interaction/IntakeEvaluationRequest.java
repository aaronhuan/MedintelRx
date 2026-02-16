package com.aaronhuang.medintel.domain.interaction;

import java.time.Instant;

import com.aaronhuang.medintel.domain.model.UserMedication;

/**
 * Represents a request to evaluate an intake as if it happened.
 *
 * <p>This separates preview intent from persisted {@link com.aaronhuang.medintel.domain.model.IntakeEvent}
 * so callers can evaluate without accidentally saving a simulated event.</p>
 */
public final class IntakeEvaluationRequest {
    private final UserMedication userMedication;
    private final Instant proposedTime;

    /**
     * Creates a request using the current time as the proposed intake time.
     *
     * @param userMedication user medication to evaluate
     */
    public IntakeEvaluationRequest(UserMedication userMedication) {
        this(userMedication, Instant.now());
    }

    /**
     * Creates a request with an explicit proposed intake time.
     *
     * @param userMedication user medication to evaluate
     * @param proposedTime proposed time of intake, defaults to now when null
     */
    public IntakeEvaluationRequest(UserMedication userMedication, Instant proposedTime) {
        this.userMedication = userMedication;
        this.proposedTime = (proposedTime == null) ? Instant.now() : proposedTime;
    }

    public UserMedication getUserMedication() {
        return userMedication;
    }

    public Instant getProposedTime() {
        return proposedTime;
    }
}
