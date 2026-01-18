package com.aaronhuang.medintel.domain.interaction;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;

/**
 * Represents a detected conflict between an intake and an avoidance window.
 */
public final class DetectedConflict {
    
    private final AvoidanceWindow violatedWindow;
    private final String message;

    /**
     * Creates a new conflict record.
     *
     * @param violatedWindow the window that was violated
     * @param message human-readable conflict message
     */
    public DetectedConflict(AvoidanceWindow violatedWindow, String message) {
        this.violatedWindow = violatedWindow;
        this.message = message;
    }

    //getters
    public AvoidanceWindow getViolatedWindow() {
        return violatedWindow;
    }

    public String getMessage() {
        return message;
    }
}
