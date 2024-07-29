package com.satoripop.ssvr.service;

import com.satoripop.ssvr.domain.UserConf;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link UserConf}.
 */
public interface UserConfService {
    /**
     * Save a userConf.
     *
     * @param userConf the entity to save.
     * @return the persisted entity.
     */
    UserConf save(UserConf userConf);

    /**
     * Updates a userConf.
     *
     * @param userConf the entity to update.
     * @return the persisted entity.
     */
    UserConf update(UserConf userConf);

    /**
     * Partially updates a userConf.
     *
     * @param userConf the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserConf> partialUpdate(UserConf userConf);

    /**
     * Get all the userConfs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserConf> findAll(Pageable pageable);

    /**
     * Get the "id" userConf.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserConf> findOne(UUID id);

    /**
     * Delete the "id" userConf.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
