package com.aaronhuang.medintel.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaronhuang.medintel.domain.model.IntakeEvent;
import com.aaronhuang.medintel.repository.IntakeEventRepository;

/**
 * Service for creating and retrieving intake events.
 */
@Service
public class IntakeEventService {
    /**
     * Place where this service keeps the repository, repository is not yet assigned but it is typed and given a name.
     *
     * <p>The repository instance is provided when the service is created.</p>
     */
    private final IntakeEventRepository intakeEventRepository;

    /**
     * Receives a repository instance and stores it in the field for later use.
     *
     * @param intakeEventRepository repository instance to use inside this service
     */
    public IntakeEventService(IntakeEventRepository intakeEventRepository) {
        this.intakeEventRepository = intakeEventRepository;
    }

    /**
     * Persists a new intake event.
     *
     * @param intakeEvent intake event to save
     * @return saved intake event
     */
    @Transactional
    public IntakeEvent create(IntakeEvent intakeEvent) {
        return intakeEventRepository.save(intakeEvent);
    }

    /**
     * Fetches an intake event by id.
     *
     * @param id intake event id
     * @return matching intake event
     */
    @Transactional(readOnly = true)
    public IntakeEvent getById(UUID id) {
        return intakeEventRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("IntakeEvent not found: " + id));
    }

    /**
     * @return all intake events
     */
    @Transactional(readOnly = true)
    public List<IntakeEvent> listAll() {
        return intakeEventRepository.findAll();
    }
    
    /**
     * Fetches all intake events for a given user profile.
     *
     * @param userProfileId user profile id
     * @return list of intake events for the user profile
     */
    @Transactional(readOnly = true)
    public List<IntakeEvent> listAllByUserProfileId(UUID userProfileId) {
        return intakeEventRepository.findByUser_Id(userProfileId);
    }
}
