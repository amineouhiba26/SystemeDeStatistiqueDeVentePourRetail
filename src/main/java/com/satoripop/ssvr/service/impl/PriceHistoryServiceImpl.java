package com.satoripop.ssvr.service.impl;

import com.satoripop.ssvr.domain.PriceHistory;
import com.satoripop.ssvr.repository.PriceHistoryRepository;
import com.satoripop.ssvr.service.PriceHistoryService;
import com.satoripop.ssvr.service.dto.PriceHistoryDTO;
import com.satoripop.ssvr.service.mapper.PriceHistoryMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriceHistory}.
 */
@Service
@Transactional
public class PriceHistoryServiceImpl implements PriceHistoryService {

    private final Logger log = LoggerFactory.getLogger(PriceHistoryServiceImpl.class);

    private final PriceHistoryRepository priceHistoryRepository;

    private final PriceHistoryMapper priceHistoryMapper;

    public PriceHistoryServiceImpl(PriceHistoryRepository priceHistoryRepository, PriceHistoryMapper priceHistoryMapper) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.priceHistoryMapper = priceHistoryMapper;
    }

    @Override
    public PriceHistoryDTO save(PriceHistoryDTO priceHistoryDTO) {
        log.debug("Request to save PriceHistory : {}", priceHistoryDTO);
        PriceHistory priceHistory = priceHistoryMapper.toEntity(priceHistoryDTO);
        priceHistory = priceHistoryRepository.save(priceHistory);
        return priceHistoryMapper.toDto(priceHistory);
    }

    @Override
    public PriceHistoryDTO update(PriceHistoryDTO priceHistoryDTO) {
        log.debug("Request to update PriceHistory : {}", priceHistoryDTO);
        PriceHistory priceHistory = priceHistoryMapper.toEntity(priceHistoryDTO);
        priceHistory = priceHistoryRepository.save(priceHistory);
        return priceHistoryMapper.toDto(priceHistory);
    }

    @Override
    public Optional<PriceHistoryDTO> partialUpdate(PriceHistoryDTO priceHistoryDTO) {
        log.debug("Request to partially update PriceHistory : {}", priceHistoryDTO);

        return priceHistoryRepository
            .findById(priceHistoryDTO.getId())
            .map(existingPriceHistory -> {
                priceHistoryMapper.partialUpdate(existingPriceHistory, priceHistoryDTO);

                return existingPriceHistory;
            })
            .map(priceHistoryRepository::save)
            .map(priceHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PriceHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PriceHistories");
        return priceHistoryRepository.findAll(pageable).map(priceHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriceHistoryDTO> findOne(UUID id) {
        log.debug("Request to get PriceHistory : {}", id);
        return priceHistoryRepository.findById(id).map(priceHistoryMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PriceHistory : {}", id);
        priceHistoryRepository.deleteById(id);
    }
}
