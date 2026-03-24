package com.aaronhuang.medintel.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.repository.UserProfileRepository;

/**
 * Service for managing user profiles.
 */
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserProfileService(UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Persists a new user profile.
     *
     * @param userProfile user profile to create
     * @return created user profile
     */
    @Transactional
    public UserProfile create(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile is required");
        }
        userProfile.setPassword(passwordEncoder.encode(userProfile.getPassword())); // hash password before saving
        return userProfileRepository.save(userProfile);
    }

    /**
     * Fetches a user profile by id.
     *
     * @param id user profile id
     * @return matching user profile
     */
    @Transactional(readOnly = true)
    public UserProfile getById(UUID id) {
        return userProfileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("UserProfile not found: " + id));
    }

    /**
     * @return all user profiles
     */
    @Transactional(readOnly = true)
    public List<UserProfile> listAll() {
        return userProfileRepository.findAll();
    }
}
