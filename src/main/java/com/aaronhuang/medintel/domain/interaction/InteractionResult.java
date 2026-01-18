package com.aaronhuang.medintel.domain.interaction;

import java.util.List;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;

/**
 * Result of evaluating a single intake against active windows and rules.
 */
public final class InteractionResult {
    private final List<DetectedConflict> conflicts;
    private final List<AvoidanceWindow> newAvoidanceWindows;

    /**
     * Creates a new interaction result.
     *
     * @param conflicts detected conflicts for the intake
     * @param newAvoidanceWindows newly generated avoidance windows
     */
    public InteractionResult(
        List<DetectedConflict> conflicts,
        List<AvoidanceWindow> newAvoidanceWindows
    ) {
        this.conflicts = conflicts;
        this.newAvoidanceWindows = newAvoidanceWindows;
    }

    /**
     * @return true if any conflicts were detected
     */
    public boolean hasConflicts() {
        return !conflicts.isEmpty();
    }

    //getters
    public List<DetectedConflict> getConflicts() {
        return conflicts;
    }

    public List<AvoidanceWindow> getNewAvoidanceWindows() {
        return newAvoidanceWindows;
    }
}
