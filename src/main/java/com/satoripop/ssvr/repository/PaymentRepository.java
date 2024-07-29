package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {}
