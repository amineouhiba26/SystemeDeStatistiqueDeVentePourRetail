package com.satoripop.ssvr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Permission.
 */
@Entity
@Table(name = "permission")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Permission extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnoreProperties(value = { "permissions" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    public UUID getId() {
        return this.id;
    }

    public Permission id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Permission name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Permission description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.removePermission(this));
        }
        if (roles != null) {
            roles.forEach(i -> i.addPermission(this));
        }
        this.roles = roles;
    }

    public Permission roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public Permission addRole(Role role) {
        this.roles.add(role);
        role.getPermissions().add(this);
        return this;
    }

    public Permission removeRole(Role role) {
        this.roles.remove(role);
        role.getPermissions().remove(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Permission)) {
            return false;
        }
        return id != null && id.equals(((Permission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Permission{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
