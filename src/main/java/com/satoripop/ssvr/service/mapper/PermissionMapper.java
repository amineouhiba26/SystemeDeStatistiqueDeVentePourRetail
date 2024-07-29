package com.satoripop.ssvr.service.mapper;

import com.satoripop.ssvr.domain.Permission;
import com.satoripop.ssvr.service.dto.PermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {}
