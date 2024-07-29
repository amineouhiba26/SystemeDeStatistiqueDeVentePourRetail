package com.satoripop.ssvr.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;

@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)
public abstract class AbstractAuditingEntityDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract T getId();

    private String createdBy;

    private Instant createdDate = Instant.now();

    private String lastModifiedBy;

    private Instant lastModifiedDate = Instant.now();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
