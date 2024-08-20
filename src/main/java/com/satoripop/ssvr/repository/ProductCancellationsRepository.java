package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.PriceHistory;
import com.satoripop.ssvr.domain.ProductCancellations;
import com.satoripop.ssvr.service.dto.ProductCancellationsDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductCancellations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductCancellationsRepository extends JpaRepository<ProductCancellations, UUID> {
    List<ProductCancellations> findByOrderItemIdIn(List<UUID> orderItemIds);
    List<ProductCancellations> findAllByOrderItem_Product_Id(UUID productId);
}
