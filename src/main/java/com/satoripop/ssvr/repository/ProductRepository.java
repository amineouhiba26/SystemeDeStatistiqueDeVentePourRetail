package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE CAST(p.capacity AS integer) < :threshold")
    List<Product> findByCapacityLessThan(@Param("threshold") Integer threshold);

    @Query("SELECT p FROM Product p WHERE p.sku LIKE %:query% OR p.name LIKE %:query%")
    List<Product> searchProducts(@Param("query") String query);
}
