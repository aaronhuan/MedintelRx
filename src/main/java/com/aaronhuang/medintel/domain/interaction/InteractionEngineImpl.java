package com.aaronhuang.medintel.domain.interaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;
import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.domain.model.UserProfile;

public class InteractionEngineImpl implements InteractionEngine {

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

    private List<DetectedConflict> detectConflicts(
        IntakeEvent intake,
        List<AvoidanceWindow> activeWindows
    ) {
        // medication-vs-medication only

        List<DetectedConflict> conflicts = new ArrayList<>(); 
        String intakeMedicationKey = intake.getMedication().getRxCui();
        LocalDateTime intakeTime = intake.getIntakeTime();

        for (AvoidanceWindow window : activeWindows) {
            if (window.getTargetMedicationKey().equals(intakeMedicationKey)) {
                // check if intakeTime is within the window
                if (!intakeTime.isBefore(window.getStartTime()) &&
                    !intakeTime.isAfter(window.getEndTime())) {
                    // conflict detected

                    String message = String.format(
                        "Intake of medication %s at %s violates avoidance window from %s to %s.",
                        intakeMedicationKey,
                        intakeTime,
                        window.getStartTime(),
                        window.getEndTime()
                    )
                    DetectedConflict conflict = new DetectedConflict(
                        window,
                        message
                    );
                    conflicts.add(conflict);
                }
            }
        }


    }

    private List<AvoidanceWindow> generateAvoidanceWindows(
        IntakeEvent intake,
        List<InteractionRule> rules
    ) {
        // rules where triggerMedicationKey matches
    }
}
