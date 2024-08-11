package com.satoripop.ssvr.service.mapper;

import com.satoripop.ssvr.domain.PriceHistory;
import com.satoripop.ssvr.domain.Product;
import com.satoripop.ssvr.service.dto.PriceHistoryDTO;
import com.satoripop.ssvr.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PriceHistory} and its DTO {@link PriceHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface PriceHistoryMapper extends EntityMapper<PriceHistoryDTO, PriceHistory> {
    @Mapping(target = "product", source = "product", qualifiedByName = "toDtoProduct")
    PriceHistoryDTO toDto(PriceHistory s);

    @Named("toDtoProduct")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProduct(Product product);

    PriceHistory toEntity(PriceHistoryDTO priceHistoryDTO);

    Object toDto(PriceHistoryDTO priceHistoryDTO);
}
