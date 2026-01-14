package com.aaronhuang.medintel.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class IntakeEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile user; //many intake events can belong to one user

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication; //many intake events can belong to one medication

    private LocalDateTime intakeTime;

    private String dosage;

    private LocalDateTime createdAt;

    protected IntakeEvent() {} //JPA default constructor

    public IntakeEvent(
        UserProfile user,
        Medication medication,
        LocalDateTime intakeTime,
        String dosage
    ) {
        this.user = user;
        this.medication = medication;
        this.intakeTime = intakeTime;
        this.dosage = dosage;
        this.createdAt = LocalDateTime.now();
    }

}
