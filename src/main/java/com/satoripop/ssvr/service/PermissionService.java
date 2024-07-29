package com.satoripop.ssvr.service;

import com.satoripop.ssvr.service.dto.PermissionDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.ssvr.domain.Permission}.
 */
public interface PermissionService {
    /**
     * Save a permission.
     *
     * @param permissionDTO the entity to save.
     * @return the persisted entity.
     */
    PermissionDTO save(PermissionDTO permissionDTO);

    /**
     * Updates a permission.
     *
     * @param permissionDTO the entity to update.
     * @return the persisted entity.
     */
    PermissionDTO update(PermissionDTO permissionDTO);

    /**
     * Partially updates a permission.
     *
     * @param permissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PermissionDTO> partialUpdate(PermissionDTO permissionDTO);

    /**
     * Get all the permissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PermissionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" permission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PermissionDTO> findOne(UUID id);

    /**
     * Delete the "id" permission.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
