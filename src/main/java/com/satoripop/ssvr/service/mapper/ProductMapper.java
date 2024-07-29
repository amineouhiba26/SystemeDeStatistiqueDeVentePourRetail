package com.satoripop.ssvr.service.mapper;

import com.satoripop.ssvr.domain.Product;
import com.satoripop.ssvr.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {}
