package com.satoripop.ssvr.service.mapper;

import com.satoripop.ssvr.domain.Order;
import com.satoripop.ssvr.domain.OrderItem;
import com.satoripop.ssvr.domain.ProductCancellations;
import com.satoripop.ssvr.service.dto.OrderDTO;
import com.satoripop.ssvr.service.dto.OrderItemDTO;
import com.satoripop.ssvr.service.dto.ProductCancellationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductCancellations} and its DTO {@link ProductCancellationsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductCancellationsMapper extends EntityMapper<ProductCancellationsDTO, ProductCancellations> {
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "orderItemId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    ProductCancellationsDTO toDto(ProductCancellations s);

    @Named("orderItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemDTO toDtoOrderItemId(OrderItem orderItem);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
