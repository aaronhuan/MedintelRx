package com.aaronhuang.medintel.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.model.Medication;
import com.aaronhuang.medintel.domain.model.UserMedication;
import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.repository.MedicationRepository;
import com.aaronhuang.medintel.repository.UserMedicationRepository;
import com.aaronhuang.medintel.repository.UserProfileRepository;

/**
 * Service for managing medications saved by users.
 */
@Service
public class UserMedicationService {
    private final UserMedicationRepository userMedicationRepository;
    private final UserProfileRepository userProfileRepository;
    private final MedicationRepository medicationRepository;
    private final RxNavClient rxNavClient;

    public UserMedicationService(
        UserMedicationRepository userMedicationRepository,
        UserProfileRepository userProfileRepository,
        MedicationRepository medicationRepository,
        RxNavClient rxNavClient
    ) {
        this.userMedicationRepository = userMedicationRepository;
        this.userProfileRepository = userProfileRepository;
        this.medicationRepository = medicationRepository;
        this.rxNavClient = rxNavClient;
    }

    /**
     * Creates a new user medication record and normalizes the medication via RxNav.
     *
     * @param userId owning user profile id
     * @param rxCui optional RxCUI (when provided, RxNav name lookup still occurs)
     * @param name optional medication name to resolve when RxCUI is not provided
     * @param dosage default dosage description
     * @param frequency free-text frequency or schedule
     * @param startDate start timestamp in UTC
     * @param endDate end timestamp in UTC
     * @param notes optional user notes
     * @param asNeeded whether the medication is taken as needed
     * @param active whether the medication is currently active
     * @return created user medication record
     */
    @Transactional
    public UserMedication create(
        UUID userId,
        String rxCui,
        String name,
        String dosage,
        String frequency,
        Instant startDate,
        Instant endDate,
        String notes,
        Boolean asNeeded,
        Boolean active
    ) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }

        UserProfile user = userProfileRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("UserProfile not found: " + userId));

        Medication medication = resolveMedication(rxCui, name);

        UserMedication userMedication = new UserMedication(user, medication, dosage);
        userMedication.setFrequency(frequency);
        userMedication.setStartDate(startDate);
        userMedication.setEndDate(endDate);
        userMedication.setNotes(notes);
        if (asNeeded != null) {
            userMedication.setAsNeeded(asNeeded);
        }
        if (active != null) {
            userMedication.setActive(active);
        }

        return userMedicationRepository.save(userMedication);
    }

    /**
     * Fetches a user medication by id.
     *
     * @param id user medication id
     * @return matching user medication
     */
    @Transactional(readOnly = true)
    public UserMedication getById(UUID id) {
        return userMedicationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("UserMedication not found: " + id));
    }

    /**
     * Fetches all medications saved by a user.
     *
     * @param userId user profile id
     * @return list of user medications
     */
    @Transactional(readOnly = true)
    public List<UserMedication> listByUser(UUID userId) {
        return userMedicationRepository.findByUser_Id(userId);
    }

    /**
     * Fetches active medications saved by a user.
     *
     * @param userId user profile id
     * @return list of active user medications
     */
    @Transactional(readOnly = true)
    public List<UserMedication> listActiveByUser(UUID userId) {
        return userMedicationRepository.findByUser_IdAndActiveTrue(userId);
    }

    /**
     * Updates user-specific fields on a medication record.
     *
     * @param id user medication id
     * @param dosage default dosage description
     * @param frequency free-text frequency or schedule
     * @param startDate start timestamp in UTC
     * @param endDate end timestamp in UTC
     * @param notes optional user notes
     * @param asNeeded whether the medication is taken as needed
     * @param active whether the medication is currently active
     * @return updated user medication record
     */
    @Transactional
    public UserMedication update(
        UUID id,
        String dosage,
        String frequency,
        Instant startDate,
        Instant endDate,
        String notes,
        Boolean asNeeded,
        Boolean active
    ) {
        UserMedication userMedication = userMedicationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("UserMedication not found: " + id));

        if (dosage != null) {
            userMedication.setDosage(dosage);
        }
        if (frequency != null) {
            userMedication.setFrequency(frequency);
        }
        if (startDate != null) {
            userMedication.setStartDate(startDate);
        }
        if (endDate != null) {
            userMedication.setEndDate(endDate);
        }
        if (notes != null) {
            userMedication.setNotes(notes);
        }
        if (asNeeded != null) {
            userMedication.setAsNeeded(asNeeded);
        }
        if (active != null) {
            userMedication.setActive(active);
        }

        return userMedicationRepository.save(userMedication);
    }

    /**
     * Deletes a user medication record.
     *
     * @param id user medication id
     */
    @Transactional
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User medication id is required");
        }
        userMedicationRepository.deleteById(id);
    }

    /**
     * Resolves a medication by RxCUI or name and ensures it exists in persistence.
     *
     * <p>If {@code rxCui} is blank, the method looks up candidate RxCUIs by name
     * and selects the first match. It then retrieves the canonical RxNorm name
     * and persists a {@link Medication} record when missing.</p>
     *
     * @param rxCui optional RxCUI to resolve (preferred identifier)
     * @param name optional medication name used when RxCUI is absent
     * @return persisted medication record with RxCUI and normalized name set
     * @throws IllegalArgumentException if both {@code rxCui} and {@code name} are blank,
     *                                  or if no RxCUI is found for the given name
     */
    private Medication resolveMedication(String rxCui, String name) {
        String resolvedRxCui = rxCui;
        if (resolvedRxCui == null || resolvedRxCui.isBlank()) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Medication name or RxCUI is required");
            }
            List<String> candidates = rxNavClient.findRxCuisByName(name);
            if (candidates.isEmpty()) {
                throw new IllegalArgumentException("No RxCUI found for name: " + name);
            }
            resolvedRxCui = candidates.get(0);
        }

        String normalizedName = rxNavClient.getRxNormName(resolvedRxCui);
        if (normalizedName == null || normalizedName.isBlank()) {
            normalizedName = (name == null || name.isBlank()) ? resolvedRxCui : name;
        }

        Medication medication = medicationRepository.findByRxCui(resolvedRxCui).orElse(null);
        if (medication == null) {
            medication = medicationRepository.save(new Medication(resolvedRxCui, normalizedName));
        }

        if (medication.getNormalizedName() == null || medication.getNormalizedName().isBlank()) {
            medication.setNormalizedName(normalizedName);
            return medicationRepository.save(medication);
        }

        return medication;
    }
}
