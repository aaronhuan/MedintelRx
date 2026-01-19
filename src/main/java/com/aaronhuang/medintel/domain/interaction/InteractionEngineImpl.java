package com.aaronhuang.medintel.domain.interaction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;
import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.domain.model.enums.AvoidType;

/**
 * Deterministic interaction engine implementation.
 */
public class InteractionEngineImpl implements InteractionEngine {

    /**
     * Evaluates a new intake event for conflicts and generated avoidance windows.
     *
     * @param user owning user profile
     * @param newIntake intake event to evaluate
     * @param activeWindows active avoidance windows for the user
     * @param rules interaction rules that may generate new windows
     * @return interaction result containing conflicts and new windows
     */
    @Override
    public InteractionResult evaluateNewIntake(
        UserProfile user,
        IntakeEvent newIntake,
        List<AvoidanceWindow> activeWindows,
        List<InteractionRule> rules
    ) {
        List<DetectedConflict> conflicts = detectConflicts(newIntake, activeWindows);
        List<AvoidanceWindow> windows = generateAvoidanceWindows(newIntake, rules);

        return new InteractionResult(conflicts, windows);
    }

    /**
     * Detects conflicts between an intake event and active avoidance windows.
     *
     * @param intake intake event being evaluated
     * @param activeWindows active avoidance windows to check
     * @return list of detected conflicts
     */
    private List<DetectedConflict> detectConflicts(
        IntakeEvent intake,
        List<AvoidanceWindow> activeWindows
    ) {
        // medication-vs-medication only (food support can be added later)

        List<DetectedConflict> conflicts = new ArrayList<>();
        String intakeMedicationKey = intake.getMedication().getRxCui();
        Instant intakeTime = intake.getIntakeTime();

        for (AvoidanceWindow window : activeWindows) {
            if (window.getType() == AvoidType.MEDICATION &&
                window.getAvoidTargetKey().equals(intakeMedicationKey)) {
                if (!intakeTime.isBefore(window.getStartTime()) &&
                    !intakeTime.isAfter(window.getEndTime())) {
                    String message = String.format(
                        "Medication %s was taken during an active avoidance window (%s -> %s).",
                        intake.getMedication().getNormalizedName(),
                        window.getStartTime(),
                        window.getEndTime()
                    );
                    DetectedConflict conflict = new DetectedConflict(
                        window,
                        message
                    );
                    conflicts.add(conflict);
                }
            }
        }

        return conflicts;
    }

    /**
     * Generates avoidance windows triggered by the given intake event.
     *
     * @param intake intake event that may trigger rules
     * @param rules interaction rules to evaluate
     * @return list of newly generated avoidance windows
     */
    private List<AvoidanceWindow> generateAvoidanceWindows(
        IntakeEvent intake,
        List<InteractionRule> rules
    ) {
        if (rules == null || rules.isEmpty()) {
            return List.of();
        }

        String intakeMedicationKey = intake.getMedication().getRxCui();
        Instant intakeTime = intake.getIntakeTime();

        List<AvoidanceWindow> windows = new ArrayList<>();
        for (InteractionRule rule : rules) {
            if (!intakeMedicationKey.equals(rule.getTriggerMedicationKey())) {
                continue;
            }

            Instant endTime = intakeTime.plus(rule.getDuration());
            AvoidanceWindow window = new AvoidanceWindow(
                rule.getAvoidType(),
                rule.getAvoidTargetKey(),
                rule.getSeverity(),
                intake,
                intakeTime,
                endTime,
                renderExplanation(rule, intake),
                rule.getRuleId()
            );
            windows.add(window);
        }

        return windows;
    }

    /**
     * Renders a human-readable explanation from a rule template.
     *
     * @param rule rule with the explanation template
     * @param intake intake event used for substitution
     * @return rendered explanation text
     */
    private String renderExplanation(
        InteractionRule rule,
        IntakeEvent intake
    ) {
        return rule.getExplanationTemplate()
            .replace("{MEDICATION}", intake.getMedication().getNormalizedName());
    }
}
