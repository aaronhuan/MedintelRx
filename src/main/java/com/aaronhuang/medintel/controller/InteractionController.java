package com.aaronhuang.medintel.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaronhuang.medintel.service.InteractionService;
import com.aaronhuang.medintel.domain.interaction.IntakeEvaluationRequest;
import com.aaronhuang.medintel.domain.interaction.InteractionResult;
import com.aaronhuang.medintel.domain.model.UserMedication;
import com.aaronhuang.medintel.repository.UserMedicationRepository;


@RestController
@RequestMapping("/api/interaction")
public class InteractionController {
    
    private final InteractionService interactionService;
    private final UserMedicationRepository userMedicationRepository;

    public InteractionController(InteractionService interactionService, UserMedicationRepository userMedicationRepository) {
        this.interactionService = interactionService;
        this.userMedicationRepository = userMedicationRepository;
    }

    @PostMapping("/preview/{userId}/{userMedicationId}")
    public InteractionResult previewInteraction(@PathVariable UUID userId, @PathVariable UUID userMedicationId) {
        UserMedication userMedication = userMedicationRepository.findById(userMedicationId)
            .orElseThrow(() -> new IllegalArgumentException("User medication not found"));
        IntakeEvaluationRequest intakeRequest = new IntakeEvaluationRequest(userMedication, Instant.now());

        return interactionService.previewIntake(userId, intakeRequest);
    }

    @PostMapping("/intake/{userId}/{userMedicationId}")
    public InteractionResult recordIntake(@PathVariable UUID userId, @PathVariable UUID userMedicationId) {
        UserMedication userMedication = userMedicationRepository.findById(userMedicationId)
            .orElseThrow(() -> new IllegalArgumentException("User medication not found"));
        IntakeEvaluationRequest intakeRequest = new IntakeEvaluationRequest(userMedication, Instant.now());

        return interactionService.recordIntake(userId, intakeRequest.getUserMedication(), intakeRequest.getProposedTime(), null);
    }
}
