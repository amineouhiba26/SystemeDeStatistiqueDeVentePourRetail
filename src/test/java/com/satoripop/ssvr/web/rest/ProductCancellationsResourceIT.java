package com.satoripop.ssvr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.satoripop.ssvr.IntegrationTest;
import com.satoripop.ssvr.domain.ProductCancellations;
import com.satoripop.ssvr.repository.ProductCancellationsRepository;
import com.satoripop.ssvr.service.dto.ProductCancellationsDTO;
import com.satoripop.ssvr.service.mapper.ProductCancellationsMapper;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductCancellationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductCancellationsResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-cancellations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductCancellationsRepository productCancellationsRepository;

    @Autowired
    private ProductCancellationsMapper productCancellationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductCancellationsMockMvc;

    private ProductCancellations productCancellations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCancellations createEntity(EntityManager em) {
        ProductCancellations productCancellations = new ProductCancellations().reason(DEFAULT_REASON);
        return productCancellations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCancellations createUpdatedEntity(EntityManager em) {
        ProductCancellations productCancellations = new ProductCancellations().reason(UPDATED_REASON);
        return productCancellations;
    }

    @BeforeEach
    public void initTest() {
        productCancellations = createEntity(em);
    }

    @Test
    @Transactional
    void createProductCancellations() throws Exception {
        int databaseSizeBeforeCreate = productCancellationsRepository.findAll().size();
        // Create the ProductCancellations
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);
        restProductCancellationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCancellations testProductCancellations = productCancellationsList.get(productCancellationsList.size() - 1);
        assertThat(testProductCancellations.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void createProductCancellationsWithExistingId() throws Exception {
        // Create the ProductCancellations with an existing ID
        productCancellationsRepository.saveAndFlush(productCancellations);
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);

        int databaseSizeBeforeCreate = productCancellationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCancellationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductCancellations() throws Exception {
        // Initialize the database
        productCancellationsRepository.saveAndFlush(productCancellations);

        // Get all the productCancellationsList
        restProductCancellationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCancellations.getId().toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @Test
    @Transactional
    void getProductCancellations() throws Exception {
        // Initialize the database
        productCancellationsRepository.saveAndFlush(productCancellations);

        // Get the productCancellations
        restProductCancellationsMockMvc
            .perform(get(ENTITY_API_URL_ID, productCancellations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productCancellations.getId().toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingProductCancellations() throws Exception {
        // Get the productCancellations
        restProductCancellationsMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductCancellations() throws Exception {
        // Initialize the database
        productCancellationsRepository.saveAndFlush(productCancellations);

        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();

        // Update the productCancellations
        ProductCancellations updatedProductCancellations = productCancellationsRepository.findById(productCancellations.getId()).get();
        // Disconnect from session so that the updates on updatedProductCancellations are not directly saved in db
        em.detach(updatedProductCancellations);
        updatedProductCancellations.reason(UPDATED_REASON);
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(updatedProductCancellations);

        restProductCancellationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCancellationsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
        ProductCancellations testProductCancellations = productCancellationsList.get(productCancellationsList.size() - 1);
        assertThat(testProductCancellations.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void putNonExistingProductCancellations() throws Exception {
        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();
        productCancellations.setId(UUID.randomUUID());

        // Create the ProductCancellations
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCancellationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCancellationsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductCancellations() throws Exception {
        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();
        productCancellations.setId(UUID.randomUUID());

        // Create the ProductCancellations
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCancellationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductCancellations() throws Exception {
        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();
        productCancellations.setId(UUID.randomUUID());

        // Create the ProductCancellations
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCancellationsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductCancellationsWithPatch() throws Exception {
        // Initialize the database
        productCancellationsRepository.saveAndFlush(productCancellations);

        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();

        // Update the productCancellations using partial update
        ProductCancellations partialUpdatedProductCancellations = new ProductCancellations();
        partialUpdatedProductCancellations.setId(productCancellations.getId());

        partialUpdatedProductCancellations.reason(UPDATED_REASON);

        restProductCancellationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCancellations.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCancellations))
            )
            .andExpect(status().isOk());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
        ProductCancellations testProductCancellations = productCancellationsList.get(productCancellationsList.size() - 1);
        assertThat(testProductCancellations.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void fullUpdateProductCancellationsWithPatch() throws Exception {
        // Initialize the database
        productCancellationsRepository.saveAndFlush(productCancellations);

        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();

        // Update the productCancellations using partial update
        ProductCancellations partialUpdatedProductCancellations = new ProductCancellations();
        partialUpdatedProductCancellations.setId(productCancellations.getId());

        partialUpdatedProductCancellations.reason(UPDATED_REASON);

        restProductCancellationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCancellations.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCancellations))
            )
            .andExpect(status().isOk());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
        ProductCancellations testProductCancellations = productCancellationsList.get(productCancellationsList.size() - 1);
        assertThat(testProductCancellations.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void patchNonExistingProductCancellations() throws Exception {
        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();
        productCancellations.setId(UUID.randomUUID());

        // Create the ProductCancellations
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCancellationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productCancellationsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductCancellations() throws Exception {
        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();
        productCancellations.setId(UUID.randomUUID());

        // Create the ProductCancellations
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCancellationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductCancellations() throws Exception {
        int databaseSizeBeforeUpdate = productCancellationsRepository.findAll().size();
        productCancellations.setId(UUID.randomUUID());

        // Create the ProductCancellations
        ProductCancellationsDTO productCancellationsDTO = productCancellationsMapper.toDto(productCancellations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCancellationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCancellationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCancellations in the database
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductCancellations() throws Exception {
        // Initialize the database
        productCancellationsRepository.saveAndFlush(productCancellations);

        int databaseSizeBeforeDelete = productCancellationsRepository.findAll().size();

        // Delete the productCancellations
        restProductCancellationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, productCancellations.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductCancellations> productCancellationsList = productCancellationsRepository.findAll();
        assertThat(productCancellationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
