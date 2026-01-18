package com.aaronhuang.medintel.domain.model.enums;

/**
 * Severity levels for interaction findings and avoidance windows.
 */
public enum Severity {
    /** Strongest warning level. */
    CONTRAINDICATED,
    /** Major interaction risk. */
    MAJOR,
    /** Moderate interaction risk. */
    MODERATE,
    /** Minor interaction risk. */
    MINOR,
    /** Informational only. */
    INFO
}
