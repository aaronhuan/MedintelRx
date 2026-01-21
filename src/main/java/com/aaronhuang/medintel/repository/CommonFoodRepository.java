package com.aaronhuang.medintel.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.aaronhuang.medintel.domain.model.CommonFood;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommonFoodRepository extends JpaRepository<CommonFood, UUID> {
    
}
