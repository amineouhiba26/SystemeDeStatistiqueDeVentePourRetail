package com.satoripop.ssvr.service.mapper;

import com.satoripop.ssvr.domain.Order;
import com.satoripop.ssvr.domain.OrderItem;
import com.satoripop.ssvr.service.dto.OrderDTO;
import com.satoripop.ssvr.service.dto.OrderItemDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "product", source = "product")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderItemDTO toDto(OrderItem s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
