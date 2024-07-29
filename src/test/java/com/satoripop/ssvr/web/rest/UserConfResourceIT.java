package com.satoripop.ssvr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.satoripop.ssvr.IntegrationTest;
import com.satoripop.ssvr.domain.UserConf;
import com.satoripop.ssvr.repository.UserConfRepository;
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
 * Integration tests for the {@link UserConfResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserConfResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE_NUMBER = 1L;
    private static final Long UPDATED_PHONE_NUMBER = 2L;

    private static final String ENTITY_API_URL = "/api/user-confs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UserConfRepository userConfRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserConfMockMvc;

    private UserConf userConf;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConf createEntity(EntityManager em) {
        UserConf userConf = new UserConf()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return userConf;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConf createUpdatedEntity(EntityManager em) {
        UserConf userConf = new UserConf()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return userConf;
    }

    @BeforeEach
    public void initTest() {
        userConf = createEntity(em);
    }

    @Test
    @Transactional
    void createUserConf() throws Exception {
        int databaseSizeBeforeCreate = userConfRepository.findAll().size();
        // Create the UserConf
        restUserConfMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isCreated());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeCreate + 1);
        UserConf testUserConf = userConfList.get(userConfList.size() - 1);
        assertThat(testUserConf.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserConf.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserConf.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserConf.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserConf.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserConf.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void createUserConfWithExistingId() throws Exception {
        // Create the UserConf with an existing ID
        userConfRepository.saveAndFlush(userConf);

        int databaseSizeBeforeCreate = userConfRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserConfMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userConfRepository.findAll().size();
        // set the field null
        userConf.setUsername(null);

        // Create the UserConf, which fails.

        restUserConfMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isBadRequest());

        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = userConfRepository.findAll().size();
        // set the field null
        userConf.setPassword(null);

        // Create the UserConf, which fails.

        restUserConfMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isBadRequest());

        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserConfs() throws Exception {
        // Initialize the database
        userConfRepository.saveAndFlush(userConf);

        // Get all the userConfList
        restUserConfMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userConf.getId().toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getUserConf() throws Exception {
        // Initialize the database
        userConfRepository.saveAndFlush(userConf);

        // Get the userConf
        restUserConfMockMvc
            .perform(get(ENTITY_API_URL_ID, userConf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userConf.getId().toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserConf() throws Exception {
        // Get the userConf
        restUserConfMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserConf() throws Exception {
        // Initialize the database
        userConfRepository.saveAndFlush(userConf);

        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();

        // Update the userConf
        UserConf updatedUserConf = userConfRepository.findById(userConf.getId()).get();
        // Disconnect from session so that the updates on updatedUserConf are not directly saved in db
        em.detach(updatedUserConf);
        updatedUserConf
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restUserConfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserConf.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserConf))
            )
            .andExpect(status().isOk());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
        UserConf testUserConf = userConfList.get(userConfList.size() - 1);
        assertThat(testUserConf.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserConf.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserConf.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserConf.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserConf.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserConf.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingUserConf() throws Exception {
        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();
        userConf.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConf.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserConf() throws Exception {
        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();
        userConf.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserConf() throws Exception {
        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();
        userConf.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserConfWithPatch() throws Exception {
        // Initialize the database
        userConfRepository.saveAndFlush(userConf);

        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();

        // Update the userConf using partial update
        UserConf partialUpdatedUserConf = new UserConf();
        partialUpdatedUserConf.setId(userConf.getId());

        partialUpdatedUserConf.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restUserConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConf.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserConf))
            )
            .andExpect(status().isOk());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
        UserConf testUserConf = userConfList.get(userConfList.size() - 1);
        assertThat(testUserConf.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserConf.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserConf.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserConf.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserConf.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserConf.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateUserConfWithPatch() throws Exception {
        // Initialize the database
        userConfRepository.saveAndFlush(userConf);

        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();

        // Update the userConf using partial update
        UserConf partialUpdatedUserConf = new UserConf();
        partialUpdatedUserConf.setId(userConf.getId());

        partialUpdatedUserConf
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restUserConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConf.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserConf))
            )
            .andExpect(status().isOk());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
        UserConf testUserConf = userConfList.get(userConfList.size() - 1);
        assertThat(testUserConf.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserConf.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserConf.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserConf.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserConf.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserConf.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingUserConf() throws Exception {
        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();
        userConf.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userConf.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserConf() throws Exception {
        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();
        userConf.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserConf() throws Exception {
        int databaseSizeBeforeUpdate = userConfRepository.findAll().size();
        userConf.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userConf))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConf in the database
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserConf() throws Exception {
        // Initialize the database
        userConfRepository.saveAndFlush(userConf);

        int databaseSizeBeforeDelete = userConfRepository.findAll().size();

        // Delete the userConf
        restUserConfMockMvc
            .perform(delete(ENTITY_API_URL_ID, userConf.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserConf> userConfList = userConfRepository.findAll();
        assertThat(userConfList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
