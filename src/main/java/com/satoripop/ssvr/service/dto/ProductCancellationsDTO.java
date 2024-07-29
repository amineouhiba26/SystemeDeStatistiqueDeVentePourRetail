package com.satoripop.ssvr.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.ssvr.domain.ProductCancellations} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCancellationsDTO extends AbstractAuditingEntityDTO<UUID> implements Serializable {

    private UUID id;

    private String reason;

    private OrderItemDTO orderItem;

    private OrderDTO order;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemDTO orderItem) {
        this.orderItem = orderItem;
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
        if (!(o instanceof ProductCancellationsDTO)) {
            return false;
        }

        ProductCancellationsDTO productCancellationsDTO = (ProductCancellationsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productCancellationsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCancellationsDTO{" +
            "id='" + getId() + "'" +
            ", reason='" + getReason() + "'" +
            ", orderItem=" + getOrderItem() +
            ", order=" + getOrder() +
            "}";
    }
}
