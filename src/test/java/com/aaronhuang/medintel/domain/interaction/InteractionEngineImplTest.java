package com.aaronhuang.medintel.domain.interaction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;
import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.domain.model.Medication;
import com.aaronhuang.medintel.domain.model.enums.AvoidType;
import com.aaronhuang.medintel.domain.model.enums.Severity;

/**
 * Unit tests for {@link InteractionEngineImpl} behavior.
 *
 * <p>These tests focus on deterministic outcomes for conflict detection and
 * avoidance window generation without any Spring context.</p>
 *
 * @see InteractionEngineImpl
 */
class InteractionEngineImplTest {
    // no external dependencies to mock in InteractionEngineImpl, only logic & models

    private final InteractionEngineImpl engine = new InteractionEngineImpl();
    
    /**
     * @return a basic user profile for tests
     */
    private static UserProfile user(){
        return new UserProfile("test-user");
    }

    /**
     * @param rxCui canonical medication key
     * @param name display name used in explanations
     * @return a medication instance for tests
     */
    private static Medication medication(String rxCui, String name){
        return new Medication(rxCui, name);
    }

    /**
     * @param user owning user profile
     * @param medication medication taken
     * @param intakeTime UTC intake time
     * @return a concrete intake event for evaluation
     */
    private static IntakeEvent intakeEvent(
        UserProfile user,
        Medication medication,
        Instant intakeTime
    ){
        return new IntakeEvent(user, medication, intakeTime, "10mg");
    }

    /**
     * @param start window start time
     * @param end window end time
     * @param avoidTargetKey target medication key to avoid
     * @param intakeEvent triggering intake event
     * @return a medication avoidance window for tests
     */
    private static AvoidanceWindow window(
        Instant start,
        Instant end,
        String avoidTargetKey,
        IntakeEvent intakeEvent
    ){
        return new AvoidanceWindow(
            AvoidType.MEDICATION,
            avoidTargetKey,
            Severity.MINOR,
            intakeEvent,
            start,
            end,
            "Test Explanation",
            "rule-123"
        );
    }

    @Test
    /**
     * Verifies that no conflicts or windows are returned when there are
     * no active windows and no rules.
     */
    void evaluateNewIntake_emptyWindowAndNoRules_returnsEmptyResult() {

        // Arrange - instantiate necessary data
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T10:00:00Z"));

        // Act - execute the method intended to be tested
        InteractionResult result = engine.evaluateNewIntake(user, intake, List.of(),List.of());

        // Assert - verify the results are as expected
        assertTrue(result.getConflicts().isEmpty(), "Expected no conflicts");
        assertTrue(result.getNewAvoidanceWindows().isEmpty(), "Expected no new avoidance windows");
        assertFalse(result.hasConflicts(), "Expected hasConflicts to be false");
    }

    @Test
    /**
     * Verifies that an intake occurring within an active window with a matching
     * medication key produces a conflict.
     */
    void evaluateNewIntake_intakeDuringActiveWindow_detectsConflict() {
    
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T10:00:00Z"));
        AvoidanceWindow activeWindow = window(
            Instant.parse("2026-01-01T09:00:00Z"),
            Instant.parse("2026-01-01T11:00:00Z"),
            "1",
            intake
        );
        List<AvoidanceWindow> activeWindows = List.of(activeWindow);


        InteractionResult result = engine.evaluateNewIntake(user, intake, activeWindows, List.of());


        assertEquals(1, result.getConflicts().size(), "Expected one conflict detected");
        DetectedConflict conflict = result.getConflicts().get(0);
        assertEquals(activeWindow, conflict.getViolatedWindow(), "Conflict should reference the active window");
        assertTrue(conflict.getMessage().contains("MedA"), "Conflict message should mention the medication name");
    }

    @Test 
    /**
     * Verifies that an intake outside the active window does not produce a conflict.
     */
    void evaluateNewIntake_intakeOutsideActiveWindow_noConflict() {
    
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T12:00:00Z"));
        AvoidanceWindow activeWindow = window(
            Instant.parse("2026-01-01T09:00:00Z"),
            Instant.parse("2026-01-01T11:00:00Z"),
            "1",
            intake
        );
        List<AvoidanceWindow> activeWindows = List.of(activeWindow);

        InteractionResult result = engine.evaluateNewIntake(user, intake, activeWindows, List.of());

        assertTrue(result.getConflicts().isEmpty(), "Expected no conflicts detected");
        assertTrue(result.getNewAvoidanceWindows().isEmpty(), "Expected no new avoidance windows");
    }

    @Test
    /**
     * Verifies that the engine returns no conflicts when there are no active windows.
     */
    void evaluateNewIntake_noActiveWindows_returnsEmptyConflictList() {
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T10:00:00Z"));

        List<DetectedConflict> conflicts = engine.evaluateNewIntake(
            user,
            intake,
            List.of(),
            List.of()
        ).getConflicts();

        assertTrue(conflicts.isEmpty(), "Expected no conflicts when there are no active windows");
    }

    @Test
    /**
     * Verifies that windows with non-matching medication keys do not create conflicts.
     */
    void evaluateNewIntake_noMatchingWindows_returnsEmptyConflictList() {
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T10:00:00Z"));
        AvoidanceWindow nonMatchingWindow = window(
            Instant.parse("2026-01-01T09:00:00Z"),
            Instant.parse("2026-01-01T11:00:00Z"),
            "2", // Different medication key
            intake
        );
        List<AvoidanceWindow> activeWindows = List.of(nonMatchingWindow);
        
        List<DetectedConflict> conflicts = engine.evaluateNewIntake(
            user,
            intake,
            activeWindows,
            List.of()
        ).getConflicts();

        assertTrue(conflicts.isEmpty(), "Expected no conflicts when no windows match the intake medication");
    }

    @Test
    /**
     * Verifies that an intake at the start or end boundary is treated as a conflict.
     */
    void evaluateNewIntake_intakeAtWindowBoundaries_detectsConflict() {
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intakeAtStart = intakeEvent(user, med, Instant.parse("2026-01-01T09:00:00Z"));
        IntakeEvent intakeAtEnd = intakeEvent(user, med, Instant.parse("2026-01-01T11:00:00Z"));
        AvoidanceWindow activeWindow = window(
            Instant.parse("2026-01-01T09:00:00Z"),
            Instant.parse("2026-01-01T11:00:00Z"),
            "1",
            intakeAtStart
        );
        List<AvoidanceWindow> activeWindows = List.of(activeWindow);
        List<DetectedConflict> conflictsAtStart = engine.evaluateNewIntake(
            user,
            intakeAtStart,
            activeWindows,
            List.of()
        ).getConflicts();
        List<DetectedConflict> conflictsAtEnd = engine.evaluateNewIntake(
            user,
            intakeAtEnd,
            activeWindows,
            List.of()
        ).getConflicts();

        assertEquals(1, conflictsAtStart.size(), "Expected conflict when intake is at window start time");
        assertEquals(1, conflictsAtEnd.size(), "Expected conflict when intake is at window end time");
    }

    @Test
    /**
     * Verifies that a matching rule generates a new avoidance window with the
     * expected fields populated.
     */
    void evaluateNewIntake_ruleMatches_generatesAvoidanceWindow() {
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        Instant intakeTime = Instant.parse("2026-01-01T10:00:00Z");
        IntakeEvent intake = intakeEvent(user, med, intakeTime);

        InteractionRule rule = new InteractionRule(
            "rule-1",
            "1",
            AvoidType.MEDICATION,
            "2",
            Duration.ofHours(4),
            Severity.MODERATE,
            "Avoid {MEDICATION} for 4 hours"
        );

        InteractionResult result = engine.evaluateNewIntake(
            user,
            intake,
            List.of(),
            List.of(rule)
        );

        assertEquals(1, result.getNewAvoidanceWindows().size(), "Expected one avoidance window");
        AvoidanceWindow generated = result.getNewAvoidanceWindows().get(0);
        assertEquals(AvoidType.MEDICATION, generated.getType(), "Expected medication avoid type");
        assertEquals("2", generated.getAvoidTargetKey(), "Expected rule target key");
        assertEquals(Severity.MODERATE, generated.getSeverityLevel(), "Expected rule severity");
        assertSame(intake, generated.getIntakeEvent(), "Expected intake event to be the trigger");
        assertEquals(intakeTime, generated.getStartTime(), "Expected window to start at intake time");
        assertEquals(intakeTime.plus(Duration.ofHours(4)), generated.getEndTime(), "Expected window end time from rule duration");
        assertEquals("Avoid MedA for 4 hours", generated.getExplanation(), "Expected explanation template substitution");
        assertEquals("rule-1", generated.getRuleId(), "Expected rule id to match");
    }

    @Test
    /**
     * Verifies that a non-matching rule does not generate any avoidance windows.
     */
    void evaluateNewIntake_ruleDoesNotMatch_generatesNoWindows() {
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T10:00:00Z"));

        InteractionRule rule = new InteractionRule(
            "rule-1",
            "9",
            AvoidType.MEDICATION,
            "2",
            Duration.ofHours(4),
            Severity.MODERATE,
            "Avoid {MEDICATION} for 4 hours"
        );

        InteractionResult result = engine.evaluateNewIntake(
            user,
            intake,
            List.of(),
            List.of(rule)
        );

        assertTrue(result.getNewAvoidanceWindows().isEmpty(), "Expected no avoidance windows when rule does not match");
    }

    @Test
    /**
     * Verifies that a null rules list is handled safely and produces no windows.
     */
    void evaluateNewIntake_nullRules_returnsNoWindows() {
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T10:00:00Z"));

        InteractionResult result = engine.evaluateNewIntake(
            user,
            intake,
            List.of(),
            null
        );

        assertTrue(result.getNewAvoidanceWindows().isEmpty(), "Expected no avoidance windows when rules are null");
    }

    @Test
    /**
     * Verifies that non-medication avoidance windows are ignored by conflict detection.
     */
    void evaluateNewIntake_nonMedicationWindow_isIgnored() {
        UserProfile user = user();
        Medication med = medication("1", "MedA");
        IntakeEvent intake = intakeEvent(user, med, Instant.parse("2026-01-01T10:00:00Z"));

        AvoidanceWindow foodWindow = new AvoidanceWindow(
            AvoidType.FOOD,
            "1",
            Severity.MINOR,
            intake,
            Instant.parse("2026-01-01T09:00:00Z"),
            Instant.parse("2026-01-01T11:00:00Z"),
            "Food window",
            "rule-food"
        );

        InteractionResult result = engine.evaluateNewIntake(
            user,
            intake,
            List.of(foodWindow),
            List.of()
        );

        assertTrue(result.getConflicts().isEmpty(), "Expected no conflicts for non-medication window");
    }
}
