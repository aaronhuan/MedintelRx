package com.aaronhuang.medintel.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaronhuang.medintel.domain.model.UserMedication;
import com.aaronhuang.medintel.service.UserMedicationService;

import lombok.Getter;
import lombok.Setter;

/**
 * API for managing medications saved by users.
 */
@RestController
@RequestMapping("/api/user-medication")
public class UserMedicationController {
    private final UserMedicationService userMedicationService;

    public UserMedicationController(UserMedicationService userMedicationService) {
        this.userMedicationService = userMedicationService;
    }

    @PostMapping
    public UserMedication create(@RequestBody CreateUserMedicationRequest request) {
        return userMedicationService.create(
            request.getUserId(),
            request.getRxCui(),
            request.getName(),
            request.getDosage(),
            request.getFrequency(),
            request.getStartDate(),
            request.getEndDate(),
            request.getNotes(),
            request.getAsNeeded(),
            request.getActive()
        );
    }

    @GetMapping("/{id}")
    public UserMedication getById(@PathVariable UUID id) {
        return userMedicationService.getById(id);
    }

    @GetMapping("/user/{userId}")
    public List<UserMedication> listByUser(@PathVariable UUID userId) {
        return userMedicationService.listByUser(userId);
    }

    @GetMapping("/user/{userId}/active")
    public List<UserMedication> listActiveByUser(@PathVariable UUID userId) {
        return userMedicationService.listActiveByUser(userId);
    }

    @PutMapping("/{id}")
    public UserMedication update(@PathVariable UUID id, @RequestBody UpdateUserMedicationRequest request) {
        return userMedicationService.update(
            id,
            request.getDosage(),
            request.getFrequency(),
            request.getStartDate(),
            request.getEndDate(),
            request.getNotes(),
            request.getAsNeeded(),
            request.getActive()
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userMedicationService.delete(id);
    }

    /**
     * Request body for creating a user medication.
     */
    @Getter
    @Setter
    public static class CreateUserMedicationRequest {
        /**
         * Owning user profile id.
         */
        private UUID userId;
        /**
         * Optional RxCUI (if present, name is optional).
         */
        private String rxCui;
        /**
         * Optional medication name to resolve (if RxCUI is absent).
         */
        private String name;
        /**
         * Default dosage description.
         */
        private String dosage;
        /**
         * Free-text frequency or schedule.
         */
        private String frequency;
        /**
         * Start timestamp in UTC.
         */
        private Instant startDate;
        /**
         * End timestamp in UTC.
         */
        private Instant endDate;
        /**
         * Optional notes.
         */
        private String notes;
        /**
         * Whether the medication is taken as needed.
         */
        private Boolean asNeeded;
        /**
         * Whether the medication is currently active.
         */
        private Boolean active;
    }

    /**
     * Request body for updating user-specific medication fields.
     */
    @Getter
    @Setter
    public static class UpdateUserMedicationRequest {
        /**
         * Default dosage description.
         */
        private String dosage;
        /**
         * Free-text frequency or schedule.
         */
        private String frequency;
        /**
         * Start timestamp in UTC.
         */
        private Instant startDate;
        /**
         * End timestamp in UTC.
         */
        private Instant endDate;
        /**
         * Optional notes.
         */
        private String notes;
        /**
         * Whether the medication is taken as needed.
         */
        private Boolean asNeeded;
        /**
         * Whether the medication is currently active.
         */
        private Boolean active;
    }
}
