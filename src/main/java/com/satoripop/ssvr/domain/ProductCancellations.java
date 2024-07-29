package com.satoripop.ssvr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;

/**
 * A ProductCancellations.
 */
@Entity
@Table(name = "product_cancellations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCancellations extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productCancellations", "product", "order" }, allowSetters = true)
    private OrderItem orderItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productCancellations", "payments" }, allowSetters = true)
    private Order order;

    public UUID getId() {
        return this.id;
    }

    public ProductCancellations id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReason() {
        return this.reason;
    }

    public ProductCancellations reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public ProductCancellations orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProductCancellations order(Order order) {
        this.setOrder(order);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCancellations)) {
            return false;
        }
        return id != null && id.equals(((ProductCancellations) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCancellations{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
