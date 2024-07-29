package com.satoripop.ssvr.service.impl;

import com.satoripop.ssvr.domain.Permission;
import com.satoripop.ssvr.repository.PermissionRepository;
import com.satoripop.ssvr.service.PermissionService;
import com.satoripop.ssvr.service.dto.PermissionDTO;
import com.satoripop.ssvr.service.mapper.PermissionMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Permission}.
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public PermissionDTO save(PermissionDTO permissionDTO) {
        log.debug("Request to save Permission : {}", permissionDTO);
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDto(permission);
    }

    @Override
    public PermissionDTO update(PermissionDTO permissionDTO) {
        log.debug("Request to update Permission : {}", permissionDTO);
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDto(permission);
    }

    @Override
    public Optional<PermissionDTO> partialUpdate(PermissionDTO permissionDTO) {
        log.debug("Request to partially update Permission : {}", permissionDTO);

        return permissionRepository
            .findById(permissionDTO.getId())
            .map(existingPermission -> {
                permissionMapper.partialUpdate(existingPermission, permissionDTO);

                return existingPermission;
            })
            .map(permissionRepository::save)
            .map(permissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PermissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Permissions");
        return permissionRepository.findAll(pageable).map(permissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionDTO> findOne(UUID id) {
        log.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id).map(permissionMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
    }
}
