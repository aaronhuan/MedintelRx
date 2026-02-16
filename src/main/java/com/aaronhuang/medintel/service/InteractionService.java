package com.aaronhuang.medintel.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.interaction.IntakeEvaluationRequest;
import com.aaronhuang.medintel.domain.interaction.InteractionEngine;
import com.aaronhuang.medintel.domain.interaction.InteractionEngineImpl;
import com.aaronhuang.medintel.domain.interaction.InteractionResult;
import com.aaronhuang.medintel.domain.interaction.InteractionRule;
import com.aaronhuang.medintel.domain.model.AvoidanceWindow;
import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.domain.model.UserMedication;

/**
 * Service that coordinates interaction evaluation and persistence.
 */
@Service
public class InteractionService {

    private final InteractionEngine interactionEngine; // deterministic evaluator used for conflict detection
    private final RuleService ruleService; // provides interaction rules for the engine
    private final AvoidanceWindowService avoidanceWindowService; // reads/writes avoidance windows
    private final IntakeEventService intakeEventService; // persists intake events
    private final UserProfileService userProfileService; // loads user profiles for evaluation

    /**
     * Creates the service with its required collaborators.
     *
     * @param ruleService service providing interaction rules
     * @param avoidanceWindowService service managing avoidance windows
     * @param intakeEventService service persisting intake events
     * @param userProfileService service fetching user profiles
     */
    public InteractionService(
        RuleService ruleService,
        AvoidanceWindowService avoidanceWindowService,
        IntakeEventService intakeEventService,
        UserProfileService userProfileService
    ) {
        this.interactionEngine = new InteractionEngineImpl();
        this.ruleService = ruleService;
        this.avoidanceWindowService = avoidanceWindowService;
        this.intakeEventService = intakeEventService;
        this.userProfileService = userProfileService;
    }

    /**
     * Evaluates a proposed intake without persisting any data.
     *
     * @param userId owning user profile identifier
     * @param request intake evaluation request
     * @return interaction result based on current state
     */
    @Transactional(readOnly = true)
    public InteractionResult previewIntake(UUID userId, IntakeEvaluationRequest request) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }
        if (request.getUserMedication() == null) {
            throw new IllegalArgumentException("User medication is required");
        }

        UserProfile user = userProfileService.getById(userId);
        UserMedication userMedication = request.getUserMedication();
        if (userMedication.getUser() != null &&
            !user.getId().equals(userMedication.getUser().getId())) {
            throw new IllegalArgumentException("User medication does not belong to user: " + userId);
        }
        IntakeEvent simulated = IntakeEvent.simulated(
            user,
            userMedication,
            request.getProposedTime()
        );
        return evaluateIntake(user, simulated);
    }

    /**
     * Records an intake event and persists any resulting avoidance windows.
     *
     * @param userId owning user profile identifier
     * @param userMedication user medication taken
     * @param intakeTime time of intake (UTC)
     * @param dosage optional dosage description
     * @return interaction result, including persisted avoidance windows
     */
    @Transactional
    public InteractionResult recordIntake(
        UUID userId,
        UserMedication userMedication,
        Instant intakeTime,
        String dosage
    ) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (userMedication == null) {
            throw new IllegalArgumentException("User medication is required");
        }
        if (intakeTime == null) {
            throw new IllegalArgumentException("Intake time is required");
        }

        UserProfile user = userProfileService.getById(userId);
        if (userMedication.getUser() != null &&
            !user.getId().equals(userMedication.getUser().getId())) {
            throw new IllegalArgumentException("User medication does not belong to user: " + userId);
        }
        IntakeEvent intakeEvent = new IntakeEvent(user, userMedication, intakeTime, dosage);
        return recordIntake(intakeEvent);
    }

    /**
     * Records an intake event and persists any resulting avoidance windows.
     *
     * @param intakeEvent intake event to save
     * @return interaction result, including persisted avoidance windows
     */
    @Transactional
    public InteractionResult recordIntake(IntakeEvent intakeEvent) {
        if (intakeEvent == null) {
            throw new IllegalArgumentException("Intake event is required");
        }
        if (intakeEvent.getUser() == null) {
            throw new IllegalArgumentException("Intake event must include a user");
        }
        if (intakeEvent.getUserMedication() == null) {
            throw new IllegalArgumentException("Intake event must include a user medication");
        }
        if (intakeEvent.getIntakeTime() == null) {
            throw new IllegalArgumentException("Intake event must include an intake time");
        }

        IntakeEvent persisted = intakeEventService.create(intakeEvent);// persist the intake event
        InteractionResult result = evaluateIntake(persisted.getUser(), persisted); // evaluate interactions from previously persisted state
        List<AvoidanceWindow> persistedWindows = persistNewWindows(result.getNewAvoidanceWindows()); // persist any new avoidance windows

        return new InteractionResult(result.getConflicts(), persistedWindows); //return result with persisted windows
    }

    /**
     * Loads windows/rules and evaluates an intake using the interaction engine.
     *
     * @param user owning user profile
     * @param intakeEvent intake event being evaluated
     * @return interaction result containing conflicts and new windows
     */
    private InteractionResult evaluateIntake(UserProfile user, IntakeEvent intakeEvent) {
        avoidanceWindowService.deleteExpiredWindows(intakeEvent.getIntakeTime()); // cleanup before evaluation

        List<AvoidanceWindow> activeWindows = avoidanceWindowService.findActiveWindows(
            user.getId(),
            intakeEvent.getIntakeTime()
        );

        List<InteractionRule> rules = ruleService.findByTriggerMedication( //load relevant rules regarding the intake medication
            intakeEvent.getUserMedication().getMedication().getRxCui()
        );

        return interactionEngine.evaluateNewIntake(
            user,
            intakeEvent,
            activeWindows,
            rules
        );
    }

    /**
     * Persists newly generated avoidance windows.
     *
     * @param windows windows to persist
     * @return persisted windows (immutable list)
     */
    private List<AvoidanceWindow> persistNewWindows(List<AvoidanceWindow> windows) {
        if (windows == null || windows.isEmpty()) {
            return List.of();
        }

        List<AvoidanceWindow> persisted = new ArrayList<>(windows.size());
        for (AvoidanceWindow window : windows) {
            persisted.add(avoidanceWindowService.create(window));
        }
        return List.copyOf(persisted);
    }
}
