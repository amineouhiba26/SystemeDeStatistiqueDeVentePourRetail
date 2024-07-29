package com.satoripop.ssvr.web.rest;

import com.satoripop.ssvr.domain.UserConf;
import com.satoripop.ssvr.repository.UserConfRepository;
import com.satoripop.ssvr.service.UserConfService;
import com.satoripop.ssvr.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.satoripop.ssvr.domain.UserConf}.
 */
@RestController
@RequestMapping("/api")
public class UserConfResource {

    private final Logger log = LoggerFactory.getLogger(UserConfResource.class);

    private static final String ENTITY_NAME = "userConf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserConfService userConfService;

    private final UserConfRepository userConfRepository;

    public UserConfResource(UserConfService userConfService, UserConfRepository userConfRepository) {
        this.userConfService = userConfService;
        this.userConfRepository = userConfRepository;
    }

    /**
     * {@code POST  /user-confs} : Create a new userConf.
     *
     * @param userConf the userConf to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userConf, or with status {@code 400 (Bad Request)} if the userConf has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-confs")
    public ResponseEntity<UserConf> createUserConf(@Valid @RequestBody UserConf userConf) throws URISyntaxException {
        log.debug("REST request to save UserConf : {}", userConf);
        if (userConf.getId() != null) {
            throw new BadRequestAlertException("A new userConf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserConf result = userConfService.save(userConf);
        return ResponseEntity
            .created(new URI("/api/user-confs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-confs/:id} : Updates an existing userConf.
     *
     * @param id the id of the userConf to save.
     * @param userConf the userConf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userConf,
     * or with status {@code 400 (Bad Request)} if the userConf is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userConf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-confs/{id}")
    public ResponseEntity<UserConf> updateUserConf(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UserConf userConf
    ) throws URISyntaxException {
        log.debug("REST request to update UserConf : {}, {}", id, userConf);
        if (userConf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userConf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userConfRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserConf result = userConfService.update(userConf);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userConf.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-confs/:id} : Partial updates given fields of an existing userConf, field will ignore if it is null
     *
     * @param id the id of the userConf to save.
     * @param userConf the userConf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userConf,
     * or with status {@code 400 (Bad Request)} if the userConf is not valid,
     * or with status {@code 404 (Not Found)} if the userConf is not found,
     * or with status {@code 500 (Internal Server Error)} if the userConf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-confs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserConf> partialUpdateUserConf(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UserConf userConf
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserConf partially : {}, {}", id, userConf);
        if (userConf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userConf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userConfRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserConf> result = userConfService.partialUpdate(userConf);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userConf.getId().toString())
        );
    }

    /**
     * {@code GET  /user-confs} : get all the userConfs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userConfs in body.
     */
    @GetMapping("/user-confs")
    public ResponseEntity<List<UserConf>> getAllUserConfs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserConfs");
        Page<UserConf> page = userConfService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-confs/:id} : get the "id" userConf.
     *
     * @param id the id of the userConf to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userConf, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-confs/{id}")
    public ResponseEntity<UserConf> getUserConf(@PathVariable UUID id) {
        log.debug("REST request to get UserConf : {}", id);
        Optional<UserConf> userConf = userConfService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userConf);
    }

    /**
     * {@code DELETE  /user-confs/:id} : delete the "id" userConf.
     *
     * @param id the id of the userConf to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-confs/{id}")
    public ResponseEntity<Void> deleteUserConf(@PathVariable UUID id) {
        log.debug("REST request to delete UserConf : {}", id);
        userConfService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
