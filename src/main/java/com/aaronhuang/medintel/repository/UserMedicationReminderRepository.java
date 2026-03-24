package com.aaronhuang.medintel.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aaronhuang.medintel.domain.model.UserMedicationReminder;

/**
 * Repository for user medication reminders.
 */
@Repository
public interface UserMedicationReminderRepository extends JpaRepository<UserMedicationReminder, UUID> {
    /**
     * Finds all reminders for a user medication.
     *
     * @param userMedicationId user medication id
     * @return list of reminders
     */
    List<UserMedicationReminder> findByUserMedication_Id(UUID userMedicationId);
}
