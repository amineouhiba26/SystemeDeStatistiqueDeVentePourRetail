package com.satoripop.ssvr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.satoripop.ssvr.IntegrationTest;
import com.satoripop.ssvr.domain.Permission;
import com.satoripop.ssvr.repository.PermissionRepository;
import com.satoripop.ssvr.service.dto.PermissionDTO;
import com.satoripop.ssvr.service.mapper.PermissionMapper;
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
 * Integration tests for the {@link PermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermissionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionMockMvc;

    private Permission permission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createEntity(EntityManager em) {
        Permission permission = new Permission().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return permission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createUpdatedEntity(EntityManager em) {
        Permission permission = new Permission().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return permission;
    }

    @BeforeEach
    public void initTest() {
        permission = createEntity(em);
    }

    @Test
    @Transactional
    void createPermission() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().size();
        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);
        restPermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate + 1);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPermissionWithExistingId() throws Exception {
        // Create the Permission with an existing ID
        permissionRepository.saveAndFlush(permission);
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        int databaseSizeBeforeCreate = permissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionRepository.findAll().size();
        // set the field null
        permission.setName(null);

        // Create the Permission, which fails.
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        restPermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissions() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get the permission
        restPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permission.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPermission() throws Exception {
        // Get the permission
        restPermissionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission
        Permission updatedPermission = permissionRepository.findById(permission.getId()).get();
        // Disconnect from session so that the updates on updatedPermission are not directly saved in db
        em.detach(updatedPermission);
        updatedPermission.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        PermissionDTO permissionDTO = permissionMapper.toDto(updatedPermission);

        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(UUID.randomUUID());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(UUID.randomUUID());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(UUID.randomUUID());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePermissionWithPatch() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission using partial update
        Permission partialUpdatedPermission = new Permission();
        partialUpdatedPermission.setId(permission.getId());

        partialUpdatedPermission.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermission))
            )
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);
        assertThat(testPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(UUID.randomUUID());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(UUID.randomUUID());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();
        permission.setId(UUID.randomUUID());

        // Create the Permission
        PermissionDTO permissionDTO = permissionMapper.toDto(permission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        int databaseSizeBeforeDelete = permissionRepository.findAll().size();

        // Delete the permission
        restPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, permission.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
