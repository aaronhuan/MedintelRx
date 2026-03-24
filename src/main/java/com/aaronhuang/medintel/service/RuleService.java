package com.aaronhuang.medintel.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.interaction.InteractionRule;
import com.aaronhuang.medintel.domain.model.enums.AvoidType;
import com.aaronhuang.medintel.domain.model.enums.Severity;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.ObjectMapper;

/**
 * Service for managing interaction rules in memory.
 */
@Service
public class RuleService {

    private static final Logger log = LoggerFactory.getLogger(RuleService.class);

    private final ObjectMapper objectMapper;
    private final RxNavClient rxNavClient;
    private final Map<String, String> rxCuiCache = new ConcurrentHashMap<>();

    public RuleService(ObjectMapper objectMapper, RxNavClient rxNavClient) {
        this.objectMapper = objectMapper;
        this.rxNavClient = rxNavClient;
    }

    @PostConstruct
    void loadRules() throws IOException {
        ClassPathResource resource = new ClassPathResource("rules.json");
        try (InputStream is = resource.getInputStream()) {
            RuleModels.RuleSet ruleSet = objectMapper.readValue(is, RuleModels.RuleSet.class);
            rules.clear();
            int skipped = 0;
            for (RuleModels.InteractionRule rule : ruleSet.rules()) {
                String triggerRxCui = resolveRxCui(rule.triggerKey());
                if (triggerRxCui == null || triggerRxCui.isBlank()) {
                    skipped++;
                    log.warn("Skipping rule {}: unable to resolve triggerKey '{}'", rule.ruleId(), rule.triggerKey());
                    continue;
                }

                String avoidTargetKey = rule.avoidTargetKey();
                AvoidType avoidType = AvoidType.valueOf(rule.avoidType().toUpperCase());
                if (avoidType == AvoidType.MEDICATION) {
                    String avoidRxCui = resolveRxCui(avoidTargetKey);
                    if (avoidRxCui == null || avoidRxCui.isBlank()) {
                        skipped++;
                        log.warn("Skipping rule {}: unable to resolve avoidTargetKey '{}'", rule.ruleId(), avoidTargetKey);
                        continue;
                    }
                    avoidTargetKey = avoidRxCui;
                }

                InteractionRule mapped = new InteractionRule(
                    rule.ruleId(),
                    triggerRxCui,
                    avoidType,
                    avoidTargetKey,
                    Duration.ofHours(rule.windowStartHours()),
                    Duration.ofHours(rule.windowEndHours()),
                    Severity.valueOf(rule.severity().toUpperCase()),
                    rule.explanation()
                );
                rules.add(mapped);
            }
            log.info("Loaded {} interaction rules from rules.json (skipped {})", rules.size(), skipped);
        }
    }

    private String resolveRxCui(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        String key = name.trim().toLowerCase();
        return rxCuiCache.computeIfAbsent(key, k -> {
            try {
                List<String> suggestions = rxNavClient.getSpellingSuggestions(name);
                String lookup = (suggestions != null && !suggestions.isEmpty()) ? suggestions.get(0) : name;
                List<String> candidates = rxNavClient.getRxCuisByName(lookup);
                if (candidates == null || candidates.isEmpty()) {
                    return null;
                }
                return candidates.get(0);
            } catch (RuntimeException ex) {
                log.warn("Failed to resolve RxCUI for '{}'", name, ex);
                return null;
            }
        });
    }

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
