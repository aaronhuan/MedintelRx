package com.aaronhuang.medintel.service;

import java.util.List;

public final class RuleModels {

    public record RuleSet(List<InteractionRule> rules){}
    
    public record InteractionRule(
            String ruleId,
            String triggerType,
            String triggerKey,
            String avoidType,
            String avoidTargetKey,
            String severity,
            int windowStartHours,
            int windowEndHours,
            String explanation,
            String recommendedAction
    ) {}
}
