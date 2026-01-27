package com.aaronhuang.medintel.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;
import com.aaronhuang.medintel.repository.AvoidanceWindowRepository;

/**
 * Service for managing avoidance windows.
 */
@Service
public class AvoidanceWindowService {
    
    private final AvoidanceWindowRepository avoidanceWindowRepository;

    public AvoidanceWindowService(AvoidanceWindowRepository avoidanceWindowRepository) {
        this.avoidanceWindowRepository = avoidanceWindowRepository;
    }

    /**
     * Persists a new avoidance window.
     *
     * @param avoidanceWindow the avoidance window to create
     * @return the created avoidance window
     */
    @Transactional
    public AvoidanceWindow create(AvoidanceWindow avoidanceWindow) {
        return avoidanceWindowRepository.save(avoidanceWindow);
    }

    /**
     * Deletes all expired avoidance windows.
     *
     * @param now the current timestamp to determine expiration
     */
    @Transactional
    public void deleteExpiredWindows(Instant now) {
        avoidanceWindowRepository.deleteExpiredWindows(now);
    }

    /**
     * Finds active avoidance windows for a user at a given timestamp.
     *
     * @param userId    the ID of the user
     * @param timestamp the timestamp to check for active windows
     * @return list of active avoidance windows
     */
    @Transactional(readOnly = true)
    public List<AvoidanceWindow> findActiveWindows(UUID userId, Instant timestamp) {
        return avoidanceWindowRepository.findActiveWindows(userId, timestamp);
    }

}
