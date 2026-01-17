package com.aaronhuang.medintel.domain.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Medication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; 

    private String rxCui;

    private String normalizedName;

    private Instant createdAt;

    protected Medication() {} //JPA default constructor

    public Medication(String rxCui, String normalizedName) {
        this.rxCui = rxCui;
        this.normalizedName = normalizedName;
        this.createdAt = Instant.now();
    }
}
