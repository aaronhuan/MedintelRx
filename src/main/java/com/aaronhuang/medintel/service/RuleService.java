package com.aaronhuang.medintel.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.interaction.InteractionRule;

/**
 * Service for managing interaction rules in memory.
 */
@Service
public class RuleService {

    private final List<InteractionRule> rules = new CopyOnWriteArrayList<>();// Thread-safe list

    /**
     * Returns all rules currently loaded in memory.
     *
     * @return list of interaction rules
     */
    @Transactional(readOnly = true)
    public List<InteractionRule> listAll() {
        return List.copyOf(rules);
    }

    /**
     * Finds rules that are triggered by the given medication key.
     *
     * @param triggerMedicationKey RxCUI medication key
     * @return list of rules triggered by the medication
     */
    @Transactional(readOnly = true)
    public List<InteractionRule> findByTriggerMedication(String triggerMedicationKey) {
        if (triggerMedicationKey == null || triggerMedicationKey.isBlank()) {
            return List.of();
        }

        return rules.stream()//pipeline through list collection
            .filter(rule -> triggerMedicationKey.equals(rule.getTriggerMedicationKey()))//filter rules by trigger medication key
            .toList(); //collect results into a list
    }

    /**
     * Adds a new rule to the in-memory collection.
     *
     * @param rule rule to add
     * @return the same rule instance
     */
    @Transactional
    public InteractionRule addRule(InteractionRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule is required");
        }
        rules.add(rule);
        return rule;
    }

    /**
     * Removes rules that match the given rule id.
     *
     * @param ruleId rule identifier
     * @return true if any rules were removed
     */
    @Transactional
    public boolean removeRule(String ruleId) {
        if (ruleId == null || ruleId.isBlank()) {
            return false;
        }
        return rules.removeIf(rule -> ruleId.equals(rule.getRuleId()));
    }
}
