package com.satoripop.ssvr.repository;

import com.satoripop.ssvr.domain.UserConf;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserConf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserConfRepository extends JpaRepository<UserConf, UUID> {}
