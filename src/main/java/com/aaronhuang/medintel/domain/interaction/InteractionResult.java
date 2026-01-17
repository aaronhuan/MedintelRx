package com.aaronhuang.medintel.domain.interaction;

import java.util.List;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;

public final class InteractionResult {
    private final List<DetectedConflict> conflicts;
    private final List<AvoidanceWindow> newAvoidanceWindows;

    public InteractionResult(
        List<DetectedConflict> conflicts,
        List<AvoidanceWindow> newAvoidanceWindows
    ) {
        this.conflicts = conflicts;
        this.newAvoidanceWindows = newAvoidanceWindows;
    }

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
