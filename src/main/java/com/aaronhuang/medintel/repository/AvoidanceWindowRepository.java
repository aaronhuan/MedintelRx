package com.aaronhuang.medintel.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;

import com.aaronhuang.medintel.domain.model.AvoidanceWindow;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AvoidanceWindowRepository extends JpaRepository<AvoidanceWindow, UUID> {
    
}
