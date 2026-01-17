package com.aaronhuang.medintel.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter //Lombok annotation to auto-generate getter methods for all fields
@Setter //Lombok annotation to auto-generate setter methods for all fields
@Entity
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //generate unique IDs automatically
    private UUID id;

    private String name;

    private String timeZoneId;

    private Instant createdAt;
    
    private Instant updatedAt;

    protected UserProfile(){} //JPA requires a default constructor for entity classes

    public UserProfile(String name) {
        this(name, "UTC");
    }

    public UserProfile(String name, String timeZoneId) {
        this.name = name;
        this.timeZoneId = (timeZoneId == null || timeZoneId.isBlank()) ? "UTC" : timeZoneId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();   
    }

}
