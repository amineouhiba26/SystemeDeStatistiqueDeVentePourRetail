package com.satoripop.ssvr.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.satoripop.ssvr.domain.Order} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDTO extends AbstractAuditingEntityDTO<UUID> implements Serializable {

    private UUID id;

    @NotNull
    private String incrementId;

    @NotNull
    private ZonedDateTime orderDate;

    @NotNull
    private String status;

    @NotNull
    private Double grandTotal;

    private Double totalInvoiced;

    private Double totalDue;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIncrementId() {
        return incrementId;
    }

    public void setIncrementId(String incrementId) {
        this.incrementId = incrementId;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getTotalInvoiced() {
        return totalInvoiced;
    }

    public void setTotalInvoiced(Double totalInvoiced) {
        this.totalInvoiced = totalInvoiced;
    }

    public Double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(Double totalDue) {
        this.totalDue = totalDue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id='" + getId() + "'" +
            ", incrementId='" + getIncrementId() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", grandTotal=" + getGrandTotal() +
            ", totalInvoiced=" + getTotalInvoiced() +
            ", totalDue=" + getTotalDue() +
            "}";
    }
}
