package com.aaronhuang.medintel.domain.interaction;

import java.util.List;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;
import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.domain.model.UserProfile;

public interface InteractionEngine {

    InteractionResult evaluateNewIntake(
    UserProfile user,
    IntakeEvent newIntakeEvent,
    List<AvoidanceWindow> activeWindows,
    List<InteractionRule> rules
    );
}
