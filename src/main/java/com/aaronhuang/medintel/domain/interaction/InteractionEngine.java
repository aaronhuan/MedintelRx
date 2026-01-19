package com.aaronhuang.medintel.domain.interaction;

import java.util.List;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;
import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.domain.model.UserProfile;

/**
 * Evaluates medication intake events against active windows and rules.
 */
public interface InteractionEngine {

    /**
     * Evaluates a new intake event for conflicts and generated avoidance windows.
     *
     * @param user owning user profile
     * @param newIntakeEvent intake event to evaluate
     * @param activeWindows currently active avoidance windows for the user
     * @param rules interaction rules that may generate new windows
     * @return interaction result containing conflicts and new windows
     */
    InteractionResult evaluateNewIntake(
    UserProfile user,
    IntakeEvent newIntakeEvent,
    List<AvoidanceWindow> activeWindows,
    List<InteractionRule> rules
    );
}
