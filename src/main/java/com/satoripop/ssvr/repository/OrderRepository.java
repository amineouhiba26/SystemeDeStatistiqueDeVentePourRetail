package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {}
