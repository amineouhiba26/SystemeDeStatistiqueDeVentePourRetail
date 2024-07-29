package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.ProductCancellations;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductCancellations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductCancellationsRepository extends JpaRepository<ProductCancellations, UUID> {}
