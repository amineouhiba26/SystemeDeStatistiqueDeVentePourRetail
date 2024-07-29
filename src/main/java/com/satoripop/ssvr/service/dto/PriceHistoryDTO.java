package com.satoripop.ssvr.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.satoripop.ssvr.domain.PriceHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceHistoryDTO extends AbstractAuditingEntityDTO<UUID> implements Serializable {

    private UUID id;

    @NotNull
    private Double oldPrice;

    @NotNull
    private Double newPrice;

    private ProductDTO product;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceHistoryDTO)) {
            return false;
        }

        PriceHistoryDTO priceHistoryDTO = (PriceHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priceHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceHistoryDTO{" +
            "id='" + getId() + "'" +
            ", oldPrice=" + getOldPrice() +
            ", newPrice=" + getNewPrice() +
            ", product=" + getProduct() +
            "}";
    }
}
