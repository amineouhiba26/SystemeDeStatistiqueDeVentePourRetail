package com.satoripop.ssvr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "payment_mehod_name")
    private String paymentMehodName;

    @Column(name = "payment_mehode_code")
    private String paymentMehodeCode;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productCancellations", "payments" }, allowSetters = true)
    private Order payment;

    public UUID getId() {
        return this.id;
    }

    public Payment id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Payment amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMehodName() {
        return this.paymentMehodName;
    }

    public Payment paymentMehodName(String paymentMehodName) {
        this.setPaymentMehodName(paymentMehodName);
        return this;
    }

    public void setPaymentMehodName(String paymentMehodName) {
        this.paymentMehodName = paymentMehodName;
    }

    public String getPaymentMehodeCode() {
        return this.paymentMehodeCode;
    }

    public Payment paymentMehodeCode(String paymentMehodeCode) {
        this.setPaymentMehodeCode(paymentMehodeCode);
        return this;
    }

    public void setPaymentMehodeCode(String paymentMehodeCode) {
        this.paymentMehodeCode = paymentMehodeCode;
    }

    public ZonedDateTime getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(ZonedDateTime paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Payment status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order getPayment() {
        return this.payment;
    }

    public void setPayment(Order order) {
        this.payment = order;
    }

    public Payment payment(Order order) {
        this.setPayment(order);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", paymentMehodName='" + getPaymentMehodName() + "'" +
            ", paymentMehodeCode='" + getPaymentMehodeCode() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
