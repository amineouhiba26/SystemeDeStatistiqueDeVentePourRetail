package com.satoripop.ssvr.service.impl;

import com.satoripop.ssvr.domain.Role;
import com.satoripop.ssvr.repository.RoleRepository;
import com.satoripop.ssvr.service.RoleService;
import com.satoripop.ssvr.service.dto.RoleDTO;
import com.satoripop.ssvr.service.mapper.RoleMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Role}.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDTO update(RoleDTO roleDTO) {
        log.debug("Request to update Role : {}", roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public Optional<RoleDTO> partialUpdate(RoleDTO roleDTO) {
        log.debug("Request to partially update Role : {}", roleDTO);

        return roleRepository
            .findById(roleDTO.getId())
            .map(existingRole -> {
                roleMapper.partialUpdate(existingRole, roleDTO);

                return existingRole;
            })
            .map(roleRepository::save)
            .map(roleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Roles");
        return roleRepository.findAll(pageable).map(roleMapper::toDto);
    }

    public Page<RoleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return roleRepository.findAllWithEagerRelationships(pageable).map(roleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findOne(UUID id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findOneWithEagerRelationships(id).map(roleMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Role : {}", id);
        roleRepository.deleteById(id);
    }
}
