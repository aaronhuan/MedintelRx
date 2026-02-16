package com.aaronhuang.medintel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaronhuang.medintel.domain.model.Medication;
import com.aaronhuang.medintel.repository.MedicationRepository;

@RestController
@RequestMapping("/api/medication")
public class MedicationController {

    private final MedicationRepository medicationRepository;

    public MedicationController(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }
    
    @GetMapping("/{rxcui}")
    public Medication getMedication(@PathVariable String rxcui) {
        return medicationRepository.findByRxCui(rxcui)
            .orElseThrow(() -> new IllegalArgumentException("Medication not found: " + rxcui));
    }
}
