package com.aaronhuang.medintel.domain.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class IntakeEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile user; //many intake events can belong to one user

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication; //many intake events can belong to one medication

    private Instant intakeTime;

    private String dosage;

    private Instant createdAt;

    protected IntakeEvent() {} //JPA default constructor

    public IntakeEvent(
        UserProfile user,
        Medication medication,
        Instant intakeTime,
        String dosage
    ) {
        this.user = user;
        this.medication = medication;
        this.intakeTime = intakeTime;
        this.dosage = dosage;
        this.createdAt = Instant.now();
    }

}
