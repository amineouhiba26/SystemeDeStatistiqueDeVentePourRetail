package com.satoripop.ssvr.service.mapper;

import com.satoripop.ssvr.domain.Order;
import com.satoripop.ssvr.service.dto.OrderDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {}
