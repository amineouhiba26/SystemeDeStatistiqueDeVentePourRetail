package com.satoripop.ssvr.service;

import com.satoripop.ssvr.service.dto.OrderItemDTO;
import com.satoripop.ssvr.service.dto.ProductDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Service Interface for managing {@link com.satoripop.ssvr.domain.OrderItem}.
 */
public interface OrderItemService {
    /**
     * Save a orderItem.
     *
     * @param orderItemDTO the entity to save.
     * @return the persisted entity.
     */
    OrderItemDTO save(OrderItemDTO orderItemDTO);

    /**
     * Updates a orderItem.
     *
     * @param orderItemDTO the entity to update.
     * @return the persisted entity.
     */
    OrderItemDTO update(OrderItemDTO orderItemDTO);

    /**
     * Partially updates a orderItem.
     *
     * @param orderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderItemDTO> partialUpdate(OrderItemDTO orderItemDTO);

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orderItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderItemDTO> findOne(UUID id);

    /**
     * Delete the "id" orderItem.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    List<OrderItemDTO> findByProductId(UUID productId);

    List<ProductDTO> getTop10MostSoldProductsForLastMonth();

    Long getTotalQuantityOrderedByProductId(UUID productId);

    List<Map<String, Object>> getSalesByDayOfWeek(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> getSalesByWeek(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> getSalesByMonth(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> getSalesByHour(LocalDate startDate, LocalDate endDate);
}
