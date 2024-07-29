package com.satoripop.ssvr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "increment_id", nullable = false)
    private String incrementId;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private ZonedDateTime orderDate;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "grand_total", nullable = false)
    private Double grandTotal;

    @Column(name = "total_invoiced")
    private Double totalInvoiced;

    @Column(name = "total_due")
    private Double totalDue;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties(value = { "orderItem", "order" }, allowSetters = true)
    private Set<ProductCancellations> productCancellations = new HashSet<>();

    @OneToMany(mappedBy = "payment")
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    public UUID getId() {
        return this.id;
    }

    public Order id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIncrementId() {
        return this.incrementId;
    }

    public Order incrementId(String incrementId) {
        this.setIncrementId(incrementId);
        return this;
    }

    public void setIncrementId(String incrementId) {
        this.incrementId = incrementId;
    }

    public ZonedDateTime getOrderDate() {
        return this.orderDate;
    }

    public Order orderDate(ZonedDateTime orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Order status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getGrandTotal() {
        return this.grandTotal;
    }

    public Order grandTotal(Double grandTotal) {
        this.setGrandTotal(grandTotal);
        return this;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getTotalInvoiced() {
        return this.totalInvoiced;
    }

    public Order totalInvoiced(Double totalInvoiced) {
        this.setTotalInvoiced(totalInvoiced);
        return this;
    }

    public void setTotalInvoiced(Double totalInvoiced) {
        this.totalInvoiced = totalInvoiced;
    }

    public Double getTotalDue() {
        return this.totalDue;
    }

    public Order totalDue(Double totalDue) {
        this.setTotalDue(totalDue);
        return this;
    }

    public void setTotalDue(Double totalDue) {
        this.totalDue = totalDue;
    }

    public Set<ProductCancellations> getProductCancellations() {
        return this.productCancellations;
    }

    public void setProductCancellations(Set<ProductCancellations> productCancellations) {
        if (this.productCancellations != null) {
            this.productCancellations.forEach(i -> i.setOrder(null));
        }
        if (productCancellations != null) {
            productCancellations.forEach(i -> i.setOrder(this));
        }
        this.productCancellations = productCancellations;
    }

    public Order productCancellations(Set<ProductCancellations> productCancellations) {
        this.setProductCancellations(productCancellations);
        return this;
    }

    public Order addProductCancellations(ProductCancellations productCancellations) {
        this.productCancellations.add(productCancellations);
        productCancellations.setOrder(this);
        return this;
    }

    public Order removeProductCancellations(ProductCancellations productCancellations) {
        this.productCancellations.remove(productCancellations);
        productCancellations.setOrder(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setPayment(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setPayment(this));
        }
        this.payments = payments;
    }

    public Order payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Order addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setPayment(this);
        return this;
    }

    public Order removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setPayment(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", incrementId='" + getIncrementId() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", grandTotal=" + getGrandTotal() +
            ", totalInvoiced=" + getTotalInvoiced() +
            ", totalDue=" + getTotalDue() +
            "}";
    }
}
