package com.satoripop.ssvr.service.impl;

import com.satoripop.ssvr.domain.OrderItem;
import com.satoripop.ssvr.domain.ProductCancellations;
import com.satoripop.ssvr.repository.OrderItemRepository;
import com.satoripop.ssvr.repository.ProductCancellationsRepository;
import com.satoripop.ssvr.service.ProductCancellationsService;
import com.satoripop.ssvr.service.dto.ProductCancellationsDTO;
import com.satoripop.ssvr.service.mapper.ProductCancellationsMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductCancellations}.
 */
@Service
@Transactional
public class ProductCancellationsServiceImpl implements ProductCancellationsService {

    private final Logger log = LoggerFactory.getLogger(ProductCancellationsServiceImpl.class);

    private final ProductCancellationsRepository productCancellationsRepository;
    private final ProductCancellationsMapper productCancellationsMapper;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ProductCancellationsServiceImpl(
        ProductCancellationsRepository productCancellationsRepository,
        ProductCancellationsMapper productCancellationsMapper,
        OrderItemRepository orderItemRepository
    ) {
        this.productCancellationsRepository = productCancellationsRepository;
        this.productCancellationsMapper = productCancellationsMapper;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public ProductCancellationsDTO save(ProductCancellationsDTO productCancellationsDTO) {
        log.debug("Request to save ProductCancellations : {}", productCancellationsDTO);
        ProductCancellations productCancellations = productCancellationsMapper.toEntity(productCancellationsDTO);
        productCancellations = productCancellationsRepository.save(productCancellations);
        return productCancellationsMapper.toDto(productCancellations);
    }

    @Override
    public ProductCancellationsDTO update(ProductCancellationsDTO productCancellationsDTO) {
        log.debug("Request to update ProductCancellations : {}", productCancellationsDTO);
        ProductCancellations productCancellations = productCancellationsMapper.toEntity(productCancellationsDTO);
        productCancellations = productCancellationsRepository.save(productCancellations);
        return productCancellationsMapper.toDto(productCancellations);
    }

    @Override
    public Optional<ProductCancellationsDTO> partialUpdate(ProductCancellationsDTO productCancellationsDTO) {
        log.debug("Request to partially update ProductCancellations : {}", productCancellationsDTO);

        return productCancellationsRepository
            .findById(productCancellationsDTO.getId())
            .map(existingProductCancellations -> {
                productCancellationsMapper.partialUpdate(existingProductCancellations, productCancellationsDTO);
                return existingProductCancellations;
            })
            .map(productCancellationsRepository::save)
            .map(productCancellationsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCancellationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductCancellations");
        return productCancellationsRepository.findAll(pageable).map(productCancellationsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCancellationsDTO> findOne(UUID id) {
        log.debug("Request to get ProductCancellations : {}", id);
        return productCancellationsRepository.findById(id).map(productCancellationsMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ProductCancellations : {}", id);
        productCancellationsRepository.deleteById(id);
    }

    public List<ProductCancellationsDTO> findProductCancellationsByProductId(UUID productId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
        List<UUID> orderItemIds = orderItems.stream().map(OrderItem::getId).collect(Collectors.toList());

        List<ProductCancellations> cancellations = productCancellationsRepository.findByOrderItemIdIn(orderItemIds);
        return cancellations
            .stream()
            .map(productCancellationsMapper::toDto) //
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductCancellationsDTO> findAllByOrderItem_Product_Id(UUID productId) {
        return productCancellationsRepository
            .findAllByOrderItem_Product_Id(productId)
            .stream()
            .map(productCancellationsMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllCancellationReasons() {
        return productCancellationsRepository.findCancellationReasons();
    }
}
