package com.ims.inventorymanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

@Entity
@Table(
        name = "reorder_rules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reorder_product_location",
                        columnNames = {"product_id", "location_id"}
                )
        }
)
public class ReorderRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "reorder_level", nullable = false, precision = 19, scale = 4)
    private BigDecimal reorderLevel;

    @Column(name = "reorder_quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal reorderQuantity;

    @Column(nullable = false)
    private Boolean active = true;

    public ReorderRule() {
    }

    public ReorderRule(
            Product product,
            Location location,
            BigDecimal reorderLevel,
            BigDecimal reorderQuantity,
            Boolean active
    ) {
        this.product = product;
        this.location = location;
        this.reorderLevel = reorderLevel;
        this.reorderQuantity = reorderQuantity;
        this.active = active != null ? active : true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public BigDecimal getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(BigDecimal reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public BigDecimal getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(BigDecimal reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}