package com.aaronhuang.medintel.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.aaronhuang.medintel.domain.model.CommonFood;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for user-specific common food entries used in interaction checks.
 *
 * <p>Stores canonical food keys and display names for explanations.</p>
 */
@Repository
public interface CommonFoodRepository extends JpaRepository<CommonFood, UUID> {
    
}
