package com.aaronhuang.medintel.repository;

import com.aaronhuang.medintel.domain.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

/**
 * Repository for normalized medication records used by the interaction engine.
 *
 * <p>Primarily supports CRUD plus RxCUI-based lookups when creating or resolving
 * medications for intake evaluation.</p>
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {
    /**
     * Finds a medication by its RxCUI code.
     *
     * @param rxcui RxNorm concept identifier used as the canonical key
     * @return medication record that matches the RxCUI
     */
    Optional<Medication> findByRxCui(String rxcui);
}
