package com.aaronhuang.medintel.domain.interaction;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;

public final class DetectedConflict {
    
    private final AvoidanceWindow violatedWindow;
    private final String message;

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
