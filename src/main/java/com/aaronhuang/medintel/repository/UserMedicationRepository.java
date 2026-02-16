package com.aaronhuang.medintel.repository;

import com.aaronhuang.medintel.domain.model.UserMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for medications saved by users.
 *
 * <p>Supports lookups for a user's active and historical medications.</p>
 */
@Repository
public interface UserMedicationRepository extends JpaRepository<UserMedication, UUID> {
    /**
     * Finds all medications saved by a user.
     *
     * @param userProfileId user profile id
     * @return list of user medications
     */
    List<UserMedication> findByUser_Id(UUID userProfileId);

    /**
     * Finds active medications saved by a user.
     *
     * @param userProfileId user profile id
     * @return list of active user medications
     */
    List<UserMedication> findByUser_IdAndActiveTrue(UUID userProfileId);
}
