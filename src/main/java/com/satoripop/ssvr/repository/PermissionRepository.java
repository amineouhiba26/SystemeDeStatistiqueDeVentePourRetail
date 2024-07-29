package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.Permission;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Permission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {}
