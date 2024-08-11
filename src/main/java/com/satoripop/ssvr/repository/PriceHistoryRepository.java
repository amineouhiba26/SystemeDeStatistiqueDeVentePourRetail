package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.PriceHistory;
import com.satoripop.ssvr.service.dto.PriceHistoryDTO;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PriceHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {
    List<PriceHistory> findByProductId(UUID productId);
}
