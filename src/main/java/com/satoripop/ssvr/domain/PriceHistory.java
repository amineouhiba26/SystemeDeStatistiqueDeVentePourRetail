package com.satoripop.ssvr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PriceHistory.
 */
@Entity
@Table(name = "price_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceHistory extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "old_price", nullable = false)
    private Double oldPrice;

    @NotNull
    @Column(name = "new_price", nullable = false)
    private Double newPrice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "priceHistories" }, allowSetters = true)
    private Product product;

    public UUID getId() {
        return this.id;
    }

    public PriceHistory id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getOldPrice() {
        return this.oldPrice;
    }

    public PriceHistory oldPrice(Double oldPrice) {
        this.setOldPrice(oldPrice);
        return this;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getNewPrice() {
        return this.newPrice;
    }

    public PriceHistory newPrice(Double newPrice) {
        this.setNewPrice(newPrice);
        return this;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PriceHistory product(Product product) {
        this.setProduct(product);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceHistory)) {
            return false;
        }
        return id != null && id.equals(((PriceHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceHistory{" +
            "id=" + getId() +
            ", oldPrice=" + getOldPrice() +
            ", newPrice=" + getNewPrice() +
            "}";
    }
}
