package com.satoripop.ssvr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.satoripop.ssvr.IntegrationTest;
import com.satoripop.ssvr.domain.PriceHistory;
import com.satoripop.ssvr.repository.PriceHistoryRepository;
import com.satoripop.ssvr.service.dto.PriceHistoryDTO;
import com.satoripop.ssvr.service.mapper.PriceHistoryMapper;
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
 * Integration tests for the {@link PriceHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceHistoryResourceIT {

    private static final Double DEFAULT_OLD_PRICE = 1D;
    private static final Double UPDATED_OLD_PRICE = 2D;

    private static final Double DEFAULT_NEW_PRICE = 1D;
    private static final Double UPDATED_NEW_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/price-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private PriceHistoryMapper priceHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceHistoryMockMvc;

    private PriceHistory priceHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceHistory createEntity(EntityManager em) {
        PriceHistory priceHistory = new PriceHistory().oldPrice(DEFAULT_OLD_PRICE).newPrice(DEFAULT_NEW_PRICE);
        return priceHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceHistory createUpdatedEntity(EntityManager em) {
        PriceHistory priceHistory = new PriceHistory().oldPrice(UPDATED_OLD_PRICE).newPrice(UPDATED_NEW_PRICE);
        return priceHistory;
    }

    @BeforeEach
    public void initTest() {
        priceHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createPriceHistory() throws Exception {
        int databaseSizeBeforeCreate = priceHistoryRepository.findAll().size();
        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);
        restPriceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getOldPrice()).isEqualTo(DEFAULT_OLD_PRICE);
        assertThat(testPriceHistory.getNewPrice()).isEqualTo(DEFAULT_NEW_PRICE);
    }

    @Test
    @Transactional
    void createPriceHistoryWithExistingId() throws Exception {
        // Create the PriceHistory with an existing ID
        priceHistoryRepository.saveAndFlush(priceHistory);
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        int databaseSizeBeforeCreate = priceHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOldPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceHistoryRepository.findAll().size();
        // set the field null
        priceHistory.setOldPrice(null);

        // Create the PriceHistory, which fails.
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        restPriceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceHistoryRepository.findAll().size();
        // set the field null
        priceHistory.setNewPrice(null);

        // Create the PriceHistory, which fails.
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        restPriceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPriceHistories() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        // Get all the priceHistoryList
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceHistory.getId().toString())))
            .andExpect(jsonPath("$.[*].oldPrice").value(hasItem(DEFAULT_OLD_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].newPrice").value(hasItem(DEFAULT_NEW_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getPriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        // Get the priceHistory
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, priceHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priceHistory.getId().toString()))
            .andExpect(jsonPath("$.oldPrice").value(DEFAULT_OLD_PRICE.doubleValue()))
            .andExpect(jsonPath("$.newPrice").value(DEFAULT_NEW_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPriceHistory() throws Exception {
        // Get the priceHistory
        restPriceHistoryMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory
        PriceHistory updatedPriceHistory = priceHistoryRepository.findById(priceHistory.getId()).get();
        // Disconnect from session so that the updates on updatedPriceHistory are not directly saved in db
        em.detach(updatedPriceHistory);
        updatedPriceHistory.oldPrice(UPDATED_OLD_PRICE).newPrice(UPDATED_NEW_PRICE);
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(updatedPriceHistory);

        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceHistoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getOldPrice()).isEqualTo(UPDATED_OLD_PRICE);
        assertThat(testPriceHistory.getNewPrice()).isEqualTo(UPDATED_NEW_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(UUID.randomUUID());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceHistoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(UUID.randomUUID());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(UUID.randomUUID());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceHistoryWithPatch() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory using partial update
        PriceHistory partialUpdatedPriceHistory = new PriceHistory();
        partialUpdatedPriceHistory.setId(priceHistory.getId());

        partialUpdatedPriceHistory.oldPrice(UPDATED_OLD_PRICE).newPrice(UPDATED_NEW_PRICE);

        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceHistory))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getOldPrice()).isEqualTo(UPDATED_OLD_PRICE);
        assertThat(testPriceHistory.getNewPrice()).isEqualTo(UPDATED_NEW_PRICE);
    }

    @Test
    @Transactional
    void fullUpdatePriceHistoryWithPatch() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory using partial update
        PriceHistory partialUpdatedPriceHistory = new PriceHistory();
        partialUpdatedPriceHistory.setId(priceHistory.getId());

        partialUpdatedPriceHistory.oldPrice(UPDATED_OLD_PRICE).newPrice(UPDATED_NEW_PRICE);

        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceHistory))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getOldPrice()).isEqualTo(UPDATED_OLD_PRICE);
        assertThat(testPriceHistory.getNewPrice()).isEqualTo(UPDATED_NEW_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(UUID.randomUUID());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceHistoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(UUID.randomUUID());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(UUID.randomUUID());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeDelete = priceHistoryRepository.findAll().size();

        // Delete the priceHistory
        restPriceHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, priceHistory.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
