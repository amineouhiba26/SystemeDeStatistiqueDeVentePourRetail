package com.satoripop.ssvr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItem extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "quantity_ordered", nullable = false)
    private Integer quantityOrdered;

    @Column(name = "quantity_cancelled")
    private Integer quantityCancelled;

    @Column(name = "quantity_invoiced")
    private Integer quantityInvoiced;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @OneToMany(mappedBy = "orderItem")
    @JsonIgnoreProperties(value = { "orderItem", "order" }, allowSetters = true)
    private Set<ProductCancellations> productCancellations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "priceHistories" }, allowSetters = true)
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productCancellations", "payments" }, allowSetters = true)
    private Order order;

    public UUID getId() {
        return this.id;
    }

    public OrderItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantityOrdered() {
        return this.quantityOrdered;
    }

    public OrderItem quantityOrdered(Integer quantityOrdered) {
        this.setQuantityOrdered(quantityOrdered);
        return this;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public Integer getQuantityCancelled() {
        return this.quantityCancelled;
    }

    public OrderItem quantityCancelled(Integer quantityCancelled) {
        this.setQuantityCancelled(quantityCancelled);
        return this;
    }

    public void setQuantityCancelled(Integer quantityCancelled) {
        this.quantityCancelled = quantityCancelled;
    }

    public Integer getQuantityInvoiced() {
        return this.quantityInvoiced;
    }

    public OrderItem quantityInvoiced(Integer quantityInvoiced) {
        this.setQuantityInvoiced(quantityInvoiced);
        return this;
    }

    public void setQuantityInvoiced(Integer quantityInvoiced) {
        this.quantityInvoiced = quantityInvoiced;
    }

    public Double getPrice() {
        return this.price;
    }

    public OrderItem price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<ProductCancellations> getProductCancellations() {
        return this.productCancellations;
    }

    public void setProductCancellations(Set<ProductCancellations> productCancellations) {
        if (this.productCancellations != null) {
            this.productCancellations.forEach(i -> i.setOrderItem(null));
        }
        if (productCancellations != null) {
            productCancellations.forEach(i -> i.setOrderItem(this));
        }
        this.productCancellations = productCancellations;
    }

    public OrderItem productCancellations(Set<ProductCancellations> productCancellations) {
        this.setProductCancellations(productCancellations);
        return this;
    }

    public OrderItem addProductCancellations(ProductCancellations productCancellations) {
        this.productCancellations.add(productCancellations);
        productCancellations.setOrderItem(this);
        return this;
    }

    public OrderItem removeProductCancellations(ProductCancellations productCancellations) {
        this.productCancellations.remove(productCancellations);
        productCancellations.setOrderItem(null);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantityOrdered=" + getQuantityOrdered() +
            ", quantityCancelled=" + getQuantityCancelled() +
            ", quantityInvoiced=" + getQuantityInvoiced() +
            ", price=" + getPrice() +
            "}";
    }
}
