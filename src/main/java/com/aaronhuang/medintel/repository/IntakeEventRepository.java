package com.aaronhuang.medintel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aaronhuang.medintel.domain.model.IntakeEvent;
import java.util.UUID;

/**
 * Repository for medication intake events recorded by users.
 *
 * <p>Stores real intake history that drives conflict detection and window creation.</p>
 */
@Repository
public interface IntakeEventRepository extends JpaRepository<IntakeEvent, UUID> {
}
