package com.satoripop.ssvr.service.impl;

import com.satoripop.ssvr.domain.UserConf;
import com.satoripop.ssvr.repository.UserConfRepository;
import com.satoripop.ssvr.service.UserConfService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserConf}.
 */
@Service
@Transactional
public class UserConfServiceImpl implements UserConfService {

    private final Logger log = LoggerFactory.getLogger(UserConfServiceImpl.class);

    private final UserConfRepository userConfRepository;

    public UserConfServiceImpl(UserConfRepository userConfRepository) {
        this.userConfRepository = userConfRepository;
    }

    @Override
    public UserConf save(UserConf userConf) {
        log.debug("Request to save UserConf : {}", userConf);
        return userConfRepository.save(userConf);
    }

    @Override
    public UserConf update(UserConf userConf) {
        log.debug("Request to update UserConf : {}", userConf);
        return userConfRepository.save(userConf);
    }

    @Override
    public Optional<UserConf> partialUpdate(UserConf userConf) {
        log.debug("Request to partially update UserConf : {}", userConf);

        return userConfRepository
            .findById(userConf.getId())
            .map(existingUserConf -> {
                if (userConf.getUsername() != null) {
                    existingUserConf.setUsername(userConf.getUsername());
                }
                if (userConf.getPassword() != null) {
                    existingUserConf.setPassword(userConf.getPassword());
                }
                if (userConf.getFirstName() != null) {
                    existingUserConf.setFirstName(userConf.getFirstName());
                }
                if (userConf.getLastName() != null) {
                    existingUserConf.setLastName(userConf.getLastName());
                }
                if (userConf.getEmail() != null) {
                    existingUserConf.setEmail(userConf.getEmail());
                }
                if (userConf.getPhoneNumber() != null) {
                    existingUserConf.setPhoneNumber(userConf.getPhoneNumber());
                }

                return existingUserConf;
            })
            .map(userConfRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserConf> findAll(Pageable pageable) {
        log.debug("Request to get all UserConfs");
        return userConfRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserConf> findOne(UUID id) {
        log.debug("Request to get UserConf : {}", id);
        return userConfRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete UserConf : {}", id);
        userConfRepository.deleteById(id);
    }
}
