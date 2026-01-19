package com.aaronhuang.medintel.domain.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a normalized medication used for interaction evaluation.
 *
 * <p>Uses RxCUI as the canonical identifier for matching interaction rules.</p>
 */
@Getter
@Setter
@Entity
public class Medication {
    
    /**
     * Primary key for this medication record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; 

    /**
     * RxNorm concept unique identifier.
     */
    private String rxCui;

    /**
     * Normalized medication name for display and explanations.
     */
    private String normalizedName;

    /**
     * UTC timestamp when the medication record was created.
     */
    private Instant createdAt;

    /**
     * Default constructor for JPA.
     */
    protected Medication() {} //JPA default constructor

    /**
     * Creates a new medication record.
     *
     * @param rxCui RxNorm concept unique identifier
     * @param normalizedName normalized name for display
     */
    public Medication(String rxCui, String normalizedName) {
        this.rxCui = rxCui;
        this.normalizedName = normalizedName;
        this.createdAt = Instant.now();
    }
}
