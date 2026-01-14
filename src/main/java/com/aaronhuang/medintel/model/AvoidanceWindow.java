package com.aaronhuang.medintel.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AvoidanceWindow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AvoidType type; //ENUM for type of avoidance

    private String avoidTargetKey; //key for food or medication to avoid

    @Enumerated(EnumType.STRING)
    private Severity severityLevel; //ENUM for severity level

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String explanation;
    private String ruleId; //ID of the rule that triggered this avoidance window 

    protected AvoidanceWindow() {} //JPA default constructor

    public AvoidanceWindow(
        AvoidType type,
        String avoidTargetKey,
        Severity severityLevel,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String explanation,
        String ruleId
    ) {
        this.type = type;
        this.avoidTargetKey = avoidTargetKey;
        this.severityLevel = severityLevel;
        this.startTime = startTime;
        this.endTime = endTime;
        this.explanation = explanation;
        this.ruleId = ruleId;
    }

}




