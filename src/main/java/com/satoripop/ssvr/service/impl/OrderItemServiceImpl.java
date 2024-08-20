package com.satoripop.ssvr.service.impl;

import com.satoripop.ssvr.domain.OrderItem;
import com.satoripop.ssvr.domain.Product;
import com.satoripop.ssvr.repository.OrderItemRepository;
import com.satoripop.ssvr.repository.ProductRepository;
import com.satoripop.ssvr.service.OrderItemService;
import com.satoripop.ssvr.service.dto.OrderItemDTO;
import com.satoripop.ssvr.service.dto.ProductDTO;
import com.satoripop.ssvr.service.mapper.OrderItemMapper;
import com.satoripop.ssvr.service.mapper.ProductMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public OrderItemServiceImpl(
        OrderItemRepository orderItemRepository,
        OrderItemMapper orderItemMapper,
        ProductRepository productRepository,
        ProductMapper productMapper
    ) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public OrderItemDTO save(OrderItemDTO orderItemDTO) {
        log.debug("Request to save OrderItem : {}", orderItemDTO);
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public OrderItemDTO update(OrderItemDTO orderItemDTO) {
        log.debug("Request to update OrderItem : {}", orderItemDTO);
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public Optional<OrderItemDTO> partialUpdate(OrderItemDTO orderItemDTO) {
        log.debug("Request to partially update OrderItem : {}", orderItemDTO);

        return orderItemRepository
            .findById(orderItemDTO.getId())
            .map(existingOrderItem -> {
                orderItemMapper.partialUpdate(existingOrderItem, orderItemDTO);
                return existingOrderItem;
            })
            .map(orderItemRepository::save)
            .map(orderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItems");
        return orderItemRepository.findAll(pageable).map(orderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderItemDTO> findOne(UUID id) {
        log.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findById(id).map(orderItemMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OrderItem : {}", id);
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItemDTO> findByProductId(UUID productId) {
        log.debug("Request to get all OrderItems by productId : {}", productId);
        List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
        return orderItems.stream().map(orderItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getTop10MostSoldProductsForLastMonth() {
        log.debug("Request to get top 10 most sold products for the last month");

        // Calculate the start and end dates for the last month
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        // Convert LocalDate to Instant
        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        // Fetch the top products with sales count
        List<Object[]> results = orderItemRepository.findTop10MostSoldProductsForLastMonth(startInstant, endInstant);

        // Process the results to create ProductDTOs
        return results
            .stream()
            .limit(10)
            .map(result -> {
                UUID productId = (UUID) result[0];
                // Long totalSold = ((Number) result[1]).longValue(); // Removed since not used

                // Fetch product details
                Product product = productRepository.findById(productId).orElse(null);
                return productMapper.toDto(product);
            })
            .collect(Collectors.toList());
    }

    /**
     * Get the total quantity ordered for a given product ID.
     *
     * @param productId the ID of the product
     * @return the total quantity ordered
     */
    public Long getTotalQuantityOrderedByProductId(UUID productId) {
        return orderItemRepository.sumQuantityOrderedByProductId(productId);
    }

    @Override
    public List<Map<String, Object>> getSalesByDayOfWeek(LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get sales by day of the week from {} to {}", startDate, endDate);
        List<Object[]> results = orderItemRepository.findSalesByDayOfWeek(
            startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()
        );

        return results.stream().map(result -> Map.of("dayOfWeek", result[0], "totalSales", result[1])).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSalesByWeek(LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get sales by week from {} to {}", startDate, endDate);

        // Convert LocalDate to Instant for start and end dates
        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        // Fetch sales data from repository
        List<Object[]> results = orderItemRepository.findSalesByWeek(startInstant, endInstant);

        // Process and map the results to a list of maps
        return results.stream().map(result -> Map.of("week", result[0], "totalSales", result[1])).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSalesByMonth(LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get sales by month from {} to {}", startDate, endDate);
        List<Object[]> results = orderItemRepository.findSalesByMonth(
            startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()
        );

        return results.stream().map(result -> Map.of("month", result[0], "totalSales", result[1])).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSalesByHour(LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get sales by hour from {} to {}", startDate, endDate);
        List<Object[]> results = orderItemRepository.findSalesByHour(
            startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()
        );

        return results.stream().map(result -> Map.of("hour", result[0], "totalSales", result[1])).collect(Collectors.toList());
    }
}
