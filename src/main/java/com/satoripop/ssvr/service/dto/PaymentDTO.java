package com.satoripop.ssvr.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.ssvr.domain.Payment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO extends AbstractAuditingEntityDTO<UUID> implements Serializable {

    private UUID id;

    private Double amount;

    private String paymentMehodName;

    private String paymentMehodeCode;

    private ZonedDateTime paymentDate;

    private String status;

    private OrderDTO payment;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMehodName() {
        return paymentMehodName;
    }

    public void setPaymentMehodName(String paymentMehodName) {
        this.paymentMehodName = paymentMehodName;
    }

    public String getPaymentMehodeCode() {
        return paymentMehodeCode;
    }

    public void setPaymentMehodeCode(String paymentMehodeCode) {
        this.paymentMehodeCode = paymentMehodeCode;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderDTO getPayment() {
        return payment;
    }

    public void setPayment(OrderDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id='" + getId() + "'" +
            ", amount=" + getAmount() +
            ", paymentMehodName='" + getPaymentMehodName() + "'" +
            ", paymentMehodeCode='" + getPaymentMehodeCode() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", payment=" + getPayment() +
            "}";
    }
}
