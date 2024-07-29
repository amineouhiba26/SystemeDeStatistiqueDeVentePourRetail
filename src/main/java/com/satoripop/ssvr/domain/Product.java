package com.satoripop.ssvr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "brand")
    private String brand;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<PriceHistory> priceHistories = new HashSet<>();

    public UUID getId() {
        return this.id;
    }

    public Product id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSku() {
        return this.sku;
    }

    public Product sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public Product price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountAmount() {
        return this.discountAmount;
    }

    public Product discountAmount(Double discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCategory() {
        return this.category;
    }

    public Product category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCapacity() {
        return this.capacity;
    }

    public Product capacity(String capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getBrand() {
        return this.brand;
    }

    public Product brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Set<PriceHistory> getPriceHistories() {
        return this.priceHistories;
    }

    public void setPriceHistories(Set<PriceHistory> priceHistories) {
        if (this.priceHistories != null) {
            this.priceHistories.forEach(i -> i.setProduct(null));
        }
        if (priceHistories != null) {
            priceHistories.forEach(i -> i.setProduct(this));
        }
        this.priceHistories = priceHistories;
    }

    public Product priceHistories(Set<PriceHistory> priceHistories) {
        this.setPriceHistories(priceHistories);
        return this;
    }

    public Product addPriceHistory(PriceHistory priceHistory) {
        this.priceHistories.add(priceHistory);
        priceHistory.setProduct(this);
        return this;
    }

    public Product removePriceHistory(PriceHistory priceHistory) {
        this.priceHistories.remove(priceHistory);
        priceHistory.setProduct(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", discountAmount=" + getDiscountAmount() +
            ", category='" + getCategory() + "'" +
            ", capacity='" + getCapacity() + "'" +
            ", brand='" + getBrand() + "'" +
            "}";
    }
}
