package com.aaronhuang.medintel.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.model.UserMedicationReminder;
import com.aaronhuang.medintel.repository.UserMedicationReminderRepository;

/**
 * Service for managing medication reminders.
 */
@Service
public class UserMedicationReminderService {
    private final UserMedicationReminderRepository reminderRepository;

    public UserMedicationReminderService(UserMedicationReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    /**
     * Lists reminders for a specific user medication.
     *
     * @param userMedicationId user medication id
     * @return list of reminders
     */
    @Transactional(readOnly = true)
    public List<UserMedicationReminder> listByUserMedication(UUID userMedicationId) {
        return reminderRepository.findByUserMedication_Id(userMedicationId);
    }

    /**
     * Marks a reminder as intaked (and deactivates it).
     *
     * @param reminderId reminder id
     * @param intaked whether the reminder is marked as taken
     * @return updated reminder
     */
    @Transactional
    public UserMedicationReminder setIntaked(UUID reminderId, boolean intaked) {
        UserMedicationReminder reminder = reminderRepository.findById(reminderId)
            .orElseThrow(() -> new IllegalArgumentException("Reminder not found: " + reminderId));

        reminder.setIntaked(intaked);
        if (intaked) {
            reminder.setActive(false);
        }

        return reminderRepository.save(reminder);
    }
}
