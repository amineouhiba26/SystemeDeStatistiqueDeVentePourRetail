package com.satoripop.ssvr.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.satoripop.ssvr.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO extends AbstractAuditingEntityDTO<UUID> implements Serializable {

    private UUID id;

    @NotNull
    private String sku;

    @NotNull
    private String name;

    @NotNull
    private Double price;

    private Double discountAmount;

    @NotNull
    private String category;

    private String capacity;

    private String brand;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id='" + getId() + "'" +
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
