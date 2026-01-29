package com.aaronhuang.medintel.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.service.IntakeEventService;


@RestController
@RequestMapping("/api/intake-event")
public class IntakeEventController {
    private final IntakeEventService intakeEventService;
    
    public IntakeEventController(IntakeEventService intakeEventService) {
        this.intakeEventService = intakeEventService;
    }   

    @GetMapping("/{eventId}")
    public IntakeEvent getIntakeEvent(@PathVariable UUID eventId) {
        return intakeEventService.getById(eventId);
    }

    @GetMapping("/{userId}/all")
    public Iterable<IntakeEvent> getAllIntakeEvents(@PathVariable UUID userId) {
        return intakeEventService.listAllByUserProfileId(userId);
    }

}
