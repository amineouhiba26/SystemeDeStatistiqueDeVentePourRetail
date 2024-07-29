package com.satoripop.ssvr.service.mapper;

import com.satoripop.ssvr.domain.Order;
import com.satoripop.ssvr.domain.Payment;
import com.satoripop.ssvr.service.dto.OrderDTO;
import com.satoripop.ssvr.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "orderId")
    PaymentDTO toDto(Payment s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
