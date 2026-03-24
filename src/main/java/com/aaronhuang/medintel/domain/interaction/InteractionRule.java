package com.aaronhuang.medintel.domain.interaction;

import java.time.Duration;

import com.aaronhuang.medintel.domain.model.enums.AvoidType;
import com.aaronhuang.medintel.domain.model.enums.Severity;

/**
 * Immutable rule that defines an avoidance window triggered by a medication.
 */
public final class InteractionRule {
    private final String ruleId;

    private final String triggerMedicationKey; // RxCUI
    private final AvoidType avoidType;
    private final String avoidTargetKey; // food or RxCUI

    private final Duration windowStart;
    private final Duration windowEnd;
    private final Severity severity;

    private final String explanationTemplate;

    /**
     * Creates a new interaction rule.
     *
     * @param ruleId unique rule identifier
     * @param triggerMedicationKey RxCUI that triggers this rule
     * @param avoidType category of item to avoid
     * @param avoidTargetKey key of the item to avoid
     * @param windowStart offset from intake time when the window begins
     * @param windowEnd offset from intake time when the window ends
     * @param severity severity level of the interaction
     * @param explanationTemplate template used for human-readable explanations
     */
    public InteractionRule(
        String ruleId,
        String triggerMedicationKey,
        AvoidType avoidType,
        String avoidTargetKey,
        Duration windowStart,
        Duration windowEnd,
        Severity severity,
        String explanationTemplate
    ) {
        this.ruleId = ruleId;
        this.triggerMedicationKey = triggerMedicationKey;
        this.avoidType = avoidType;
        this.avoidTargetKey = avoidTargetKey;
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
        this.severity = severity;
        this.explanationTemplate = explanationTemplate;
    }

    //getters
    public String getRuleId() {
        return ruleId;
    }

    public String getTriggerMedicationKey() {
        return triggerMedicationKey;
    }

    public AvoidType getAvoidType() {
        return avoidType;
    }

    public String getAvoidTargetKey() {
        return avoidTargetKey;
    }

    public Duration getWindowStart() {
        return windowStart;
    }

    public Duration getWindowEnd() {
        return windowEnd;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getExplanationTemplate() {
        return explanationTemplate;
    }
}
