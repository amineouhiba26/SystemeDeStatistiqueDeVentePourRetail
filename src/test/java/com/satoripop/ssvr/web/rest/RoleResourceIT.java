package com.satoripop.ssvr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.satoripop.ssvr.IntegrationTest;
import com.satoripop.ssvr.domain.Role;
import com.satoripop.ssvr.repository.RoleRepository;
import com.satoripop.ssvr.service.RoleService;
import com.satoripop.ssvr.service.dto.RoleDTO;
import com.satoripop.ssvr.service.mapper.RoleMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Autowired
    private RoleMapper roleMapper;

    @Mock
    private RoleService roleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMockMvc;

    private Role role;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = new Role().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return role;
    }

    @BeforeEach
    public void initTest() {
        role = createEntity(em);
    }

    @Test
    @Transactional
    void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();
        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);
        restRoleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createRoleWithExistingId() throws Exception {
        // Create the Role with an existing ID
        roleRepository.saveAndFlush(role);
        RoleDTO roleDTO = roleMapper.toDto(role);

        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setName(null);

        // Create the Role, which fails.
        RoleDTO roleDTO = roleMapper.toDto(role);

        restRoleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(roleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(roleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).get();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        RoleDTO roleDTO = roleMapper.toDto(updatedRole);

        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(UUID.randomUUID());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(UUID.randomUUID());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(UUID.randomUUID());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.description(UPDATED_DESCRIPTION);

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRole.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRole))
            )
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(UUID.randomUUID());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(UUID.randomUUID());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(UUID.randomUUID());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Delete the role
        restRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, role.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
