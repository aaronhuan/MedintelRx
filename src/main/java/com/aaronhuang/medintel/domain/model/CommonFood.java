package com.aaronhuang.medintel.domain.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user-specific food entry used for interaction checks.
 *
 * <p>Stores a canonical key for rule matching and a display name for explanations.</p>
 */
@Getter
@Setter
@Entity
public class CommonFood {
    
    /**
     * Primary key for this food entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Owning user profile for this food entry.
     */
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile user; //many "common" foods can belong to one user

    /**
     * Canonical food key used for interaction checks.
     */
    private String foodKey;

    /**
     * Human-readable name for display purposes.
     */
    private String displayName;

    /**
     * UTC timestamp when this entry was created.
     */
    private Instant createdAt;
    
    /**
     * Default constructor for JPA.
     */
    protected CommonFood() {} 

    /**
     * Creates a new user-specific common food entry.
     *
     * @param user the user this food belongs to
     * @param foodKey the canonical food key for interaction checks
     * @param displayName the human-readable name for display
     */
    public CommonFood(UserProfile user, String foodKey, String displayName) {
        this.user = user;
        this.foodKey = foodKey;
        this.displayName = displayName;
        this.createdAt = Instant.now();
    }

}
