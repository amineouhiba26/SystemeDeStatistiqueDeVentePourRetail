package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.OrderItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {}
