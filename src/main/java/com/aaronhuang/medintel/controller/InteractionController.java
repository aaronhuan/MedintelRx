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
import com.aaronhuang.medintel.domain.model.Medication;
import com.aaronhuang.medintel.repository.MedicationRepository;


@RestController
@RequestMapping("/api/interaction")
public class InteractionController {
    
    private final InteractionService interactionService;
    private final MedicationRepository medicationRepository;

    public InteractionController(InteractionService interactionService, MedicationRepository medicationRepository) {
        this.interactionService = interactionService;
        this.medicationRepository = medicationRepository;
    }

    @PostMapping("/preview/{userId}/{medicationId}")
    public InteractionResult previewInteraction(@PathVariable UUID userId, @PathVariable UUID medicationId) {
        Medication medication = medicationRepository.findById(medicationId)
            .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
        IntakeEvaluationRequest intakeRequest = new IntakeEvaluationRequest(medication, Instant.now());

        return interactionService.previewIntake(userId, intakeRequest);
    }

    @PostMapping("/intake/{userId}/{medicationId}")
    public InteractionResult recordIntake(@PathVariable UUID userId, @PathVariable UUID medicationId) {
        Medication medication = medicationRepository.findById(medicationId)
            .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
        IntakeEvaluationRequest intakeRequest = new IntakeEvaluationRequest(medication, Instant.now());

        return interactionService.recordIntake(userId, intakeRequest.getMedication(), intakeRequest.getProposedTime(), null);
    }
}
