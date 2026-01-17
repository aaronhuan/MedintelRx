package com.aaronhuang.medintel.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CommonFood {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile user; //many "common" foods can belong to one user

    private String foodKey;

    private String displayName;

    private LocalDateTime createdAt;
    
    protected CommonFood() {} //JPA default constructor

    public CommonFood(UserProfile user, String foodKey, String displayName) {
        this.user = user;
        this.foodKey = foodKey;
        this.displayName = displayName;
        this.createdAt = LocalDateTime.now();
    }

}
