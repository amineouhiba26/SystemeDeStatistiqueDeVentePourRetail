package com.satoripop.ssvr.service;

import com.satoripop.ssvr.domain.ProductCancellations;
import com.satoripop.ssvr.service.dto.ProductCancellationsDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.satoripop.ssvr.domain.ProductCancellations}.
 */
public interface ProductCancellationsService {
    /**
     * Save a productCancellations.
     *
     * @param productCancellationsDTO the entity to save.
     * @return the persisted entity.
     */
    ProductCancellationsDTO save(ProductCancellationsDTO productCancellationsDTO);

    /**
     * Updates a productCancellations.
     *
     * @param productCancellationsDTO the entity to update.
     * @return the persisted entity.
     */
    ProductCancellationsDTO update(ProductCancellationsDTO productCancellationsDTO);

    /**
     * Partially updates a productCancellations.
     *
     * @param productCancellationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductCancellationsDTO> partialUpdate(ProductCancellationsDTO productCancellationsDTO);

    /**
     * Get all the productCancellations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductCancellationsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" productCancellations.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductCancellationsDTO> findOne(UUID id);

    /**
     * Delete the "id" productCancellations.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    List<ProductCancellationsDTO> findProductCancellationsByProductId(UUID productId);

    List<ProductCancellationsDTO> findAllByOrderItem_Product_Id(UUID productId);
}
