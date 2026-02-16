package com.aaronhuang.medintel.controller;

import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aaronhuang.medintel.service.AvoidanceWindowService;
import com.aaronhuang.medintel.domain.model.AvoidanceWindow;

@RestController
@RequestMapping("/api/avoidance-window")
public class AvoidanceWindowController {
    private final AvoidanceWindowService avoidanceWindowService;

    public AvoidanceWindowController(AvoidanceWindowService avoidanceWindowService) {
        this.avoidanceWindowService = avoidanceWindowService;
    }

    @GetMapping("/{userId}/active")
    public Iterable<AvoidanceWindow> getActiveAvoidanceWindows(@PathVariable UUID userId) {
        return avoidanceWindowService.findActiveWindows(userId, Instant.now());
    }
    
    
}
