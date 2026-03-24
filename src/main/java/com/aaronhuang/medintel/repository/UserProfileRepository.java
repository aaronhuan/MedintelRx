package com.aaronhuang.medintel.repository;

import com.aaronhuang.medintel.domain.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for user profiles that own intake history and preferences.
 *
 * <p>Supports creation and lookup of users, including their configured time zone.</p>
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByEmailIgnoreCase(String email);
}
