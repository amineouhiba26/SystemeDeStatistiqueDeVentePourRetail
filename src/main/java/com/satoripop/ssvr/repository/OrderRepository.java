package com.satoripop.ssvr.repository;

import static org.hibernate.loader.Loader.SELECT;

import com.satoripop.ssvr.domain.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT o FROM Order o JOIN o.payments p WHERE p.paymentMehodName = :paymentMethodName")
    List<Order> findOrdersByPaymentMethod(@Param("paymentMethodName") String paymentMethodName);
}
