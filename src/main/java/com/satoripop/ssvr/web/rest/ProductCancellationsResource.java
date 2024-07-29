package com.satoripop.ssvr.web.rest;

import com.satoripop.ssvr.repository.ProductCancellationsRepository;
import com.satoripop.ssvr.service.ProductCancellationsService;
import com.satoripop.ssvr.service.dto.ProductCancellationsDTO;
import com.satoripop.ssvr.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link com.satoripop.ssvr.domain.ProductCancellations}.
 */
@RestController
@RequestMapping("/api")
public class ProductCancellationsResource {

    private final Logger log = LoggerFactory.getLogger(ProductCancellationsResource.class);

    private static final String ENTITY_NAME = "productCancellations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductCancellationsService productCancellationsService;

    private final ProductCancellationsRepository productCancellationsRepository;

    public ProductCancellationsResource(
        ProductCancellationsService productCancellationsService,
        ProductCancellationsRepository productCancellationsRepository
    ) {
        this.productCancellationsService = productCancellationsService;
        this.productCancellationsRepository = productCancellationsRepository;
    }

    /**
     * {@code POST  /product-cancellations} : Create a new productCancellations.
     *
     * @param productCancellationsDTO the productCancellationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCancellationsDTO, or with status {@code 400 (Bad Request)} if the productCancellations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-cancellations")
    public ResponseEntity<ProductCancellationsDTO> createProductCancellations(@RequestBody ProductCancellationsDTO productCancellationsDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductCancellations : {}", productCancellationsDTO);
        if (productCancellationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new productCancellations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCancellationsDTO result = productCancellationsService.save(productCancellationsDTO);
        return ResponseEntity
            .created(new URI("/api/product-cancellations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-cancellations/:id} : Updates an existing productCancellations.
     *
     * @param id the id of the productCancellationsDTO to save.
     * @param productCancellationsDTO the productCancellationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCancellationsDTO,
     * or with status {@code 400 (Bad Request)} if the productCancellationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCancellationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-cancellations/{id}")
    public ResponseEntity<ProductCancellationsDTO> updateProductCancellations(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ProductCancellationsDTO productCancellationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductCancellations : {}, {}", id, productCancellationsDTO);
        if (productCancellationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCancellationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCancellationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductCancellationsDTO result = productCancellationsService.update(productCancellationsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCancellationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-cancellations/:id} : Partial updates given fields of an existing productCancellations, field will ignore if it is null
     *
     * @param id the id of the productCancellationsDTO to save.
     * @param productCancellationsDTO the productCancellationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCancellationsDTO,
     * or with status {@code 400 (Bad Request)} if the productCancellationsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productCancellationsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productCancellationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-cancellations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductCancellationsDTO> partialUpdateProductCancellations(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ProductCancellationsDTO productCancellationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductCancellations partially : {}, {}", id, productCancellationsDTO);
        if (productCancellationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCancellationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCancellationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductCancellationsDTO> result = productCancellationsService.partialUpdate(productCancellationsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCancellationsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-cancellations} : get all the productCancellations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCancellations in body.
     */
    @GetMapping("/product-cancellations")
    public ResponseEntity<List<ProductCancellationsDTO>> getAllProductCancellations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProductCancellations");
        Page<ProductCancellationsDTO> page = productCancellationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-cancellations/:id} : get the "id" productCancellations.
     *
     * @param id the id of the productCancellationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCancellationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-cancellations/{id}")
    public ResponseEntity<ProductCancellationsDTO> getProductCancellations(@PathVariable UUID id) {
        log.debug("REST request to get ProductCancellations : {}", id);
        Optional<ProductCancellationsDTO> productCancellationsDTO = productCancellationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productCancellationsDTO);
    }

    /**
     * {@code DELETE  /product-cancellations/:id} : delete the "id" productCancellations.
     *
     * @param id the id of the productCancellationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-cancellations/{id}")
    public ResponseEntity<Void> deleteProductCancellations(@PathVariable UUID id) {
        log.debug("REST request to delete ProductCancellations : {}", id);
        productCancellationsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
