package com.satoripop.ssvr.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.satoripop.ssvr.domain.OrderItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItemDTO extends AbstractAuditingEntityDTO<UUID> implements Serializable {

    private UUID id;

    @NotNull
    private Integer quantityOrdered;

    private Integer quantityCancelled;

    private Integer quantityInvoiced;

    @NotNull
    private Double price;

    private ProductDTO product;

    private OrderDTO order;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public Integer getQuantityCancelled() {
        return quantityCancelled;
    }

    public void setQuantityCancelled(Integer quantityCancelled) {
        this.quantityCancelled = quantityCancelled;
    }

    public Integer getQuantityInvoiced() {
        return quantityInvoiced;
    }

    public void setQuantityInvoiced(Integer quantityInvoiced) {
        this.quantityInvoiced = quantityInvoiced;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id='" + getId() + "'" +
            ", quantityOrdered=" + getQuantityOrdered() +
            ", quantityCancelled=" + getQuantityCancelled() +
            ", quantityInvoiced=" + getQuantityInvoiced() +
            ", price=" + getPrice() +
            ", product=" + getProduct() +
            ", order=" + getOrder() +
            "}";
    }
}
