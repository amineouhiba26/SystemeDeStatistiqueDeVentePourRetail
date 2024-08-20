package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.OrderItem;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItem entity.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    /**
     * Find all OrderItems by Product ID.
     *
     * @param productId the product ID
     * @return the list of OrderItems
     */
    List<OrderItem> findByProductId(UUID productId);

    /**
     * Custom query to find top 10 most sold products for the last month.
     *
     * @return the list of top products with their sale count
     */
    @Query(
        "SELECT oi.product.id, SUM(oi.quantityOrdered) " +
        "FROM OrderItem oi " +
        "WHERE oi.createdDate BETWEEN :startDate AND :endDate " +
        "GROUP BY oi.product.id " +
        "ORDER BY SUM(oi.quantityOrdered) DESC"
    )
    List<Object[]> findTop10MostSoldProductsForLastMonth(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    /**
     * Sum the quantity ordered by product ID.
     *
     * @param productId the ID of the product
     * @return the total quantity ordered
     */
    @Query("SELECT COALESCE(SUM(o.quantityOrdered), 0) FROM OrderItem o WHERE o.product.id = :productId")
    Long sumQuantityOrderedByProductId(@Param("productId") UUID productId);

    @Query(
        "SELECT FUNCTION('TO_CHAR', oi.createdDate, 'Day') AS dayOfWeek, SUM(oi.price * oi.quantityOrdered) AS totalSales " +
        "FROM OrderItem oi " +
        "WHERE oi.createdDate BETWEEN :startDate AND :endDate " +
        "GROUP BY FUNCTION('TO_CHAR', oi.createdDate, 'Day') " +
        "ORDER BY FUNCTION('TO_CHAR', oi.createdDate, 'Day')"
    )
    List<Object[]> findSalesByDayOfWeek(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        "SELECT FUNCTION('TO_CHAR', oi.createdDate, 'Week') AS week, SUM(oi.price * oi.quantityOrdered) AS totalSales " +
        "FROM OrderItem oi " +
        "WHERE oi.createdDate BETWEEN :startDate AND :endDate " +
        "GROUP BY FUNCTION('TO_CHAR', oi.createdDate, 'Week') " +
        "ORDER BY FUNCTION('TO_CHAR', oi.createdDate, 'Week')"
    )
    List<Object[]> findSalesByWeek(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        "SELECT FUNCTION('TO_CHAR', oi.createdDate, 'Month') AS month, SUM(oi.price * oi.quantityOrdered) AS totalSales " +
        "FROM OrderItem oi " +
        "WHERE oi.createdDate BETWEEN :startDate AND :endDate " +
        "GROUP BY FUNCTION('TO_CHAR', oi.createdDate, 'Month') " +
        "ORDER BY FUNCTION('TO_CHAR', oi.createdDate, 'Month')"
    )
    List<Object[]> findSalesByMonth(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query(
        "SELECT FUNCTION('TO_CHAR', oi.createdDate, 'HH24') AS hour, SUM(oi.price * oi.quantityOrdered) AS totalSales " +
        "FROM OrderItem oi " +
        "WHERE oi.createdDate BETWEEN :startDate AND :endDate " +
        "GROUP BY FUNCTION('TO_CHAR', oi.createdDate, 'HH24') " +
        "ORDER BY hour"
    )
    List<Object[]> findSalesByHour(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}
