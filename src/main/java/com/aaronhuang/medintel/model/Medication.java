package com.aaronhuang.medintel.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Medication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String rxCui;

    private String normalizedName;

    private LocalDateTime createdAt;

    protected Medication() {} //JPA default constructor

    public Medication(String rxCui, String normalizedName) {
        this.rxCui = rxCui;
        this.normalizedName = normalizedName;
        this.createdAt = LocalDateTime.now();
    }
}
