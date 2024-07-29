package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface RoleRepositoryWithBagRelationships {
    Optional<Role> fetchBagRelationships(Optional<Role> role);

    List<Role> fetchBagRelationships(List<Role> roles);

    Page<Role> fetchBagRelationships(Page<Role> roles);
}
