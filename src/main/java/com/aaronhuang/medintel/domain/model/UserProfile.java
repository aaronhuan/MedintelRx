package com.aaronhuang.medintel.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter //Lombok annotation to auto-generate getter methods for all fields
@Setter //Lombok annotation to auto-generate setter methods for all fields
@Entity
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //generate unique IDs automatically
    private UUID id;

    private String name;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    protected UserProfile(){} //JPA requires a default constructor for entity classes

    public UserProfile(String name) {
        //Constructor to initialize UserProfile with a name
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();   
    }

}
