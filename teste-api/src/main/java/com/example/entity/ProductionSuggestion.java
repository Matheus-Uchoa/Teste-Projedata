package com.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "production_suggestions")
public class ProductionSuggestion {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_value")
    private BigDecimal productValue;

    @Column(name = "suggested_quantity")
    private Long suggestedQuantity;

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @Column(name = "priority_rank")
    private Long priorityRank;

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductValue() {
        return productValue;
    }

    public void setProductValue(BigDecimal productValue) {
        this.productValue = productValue;
    }

    public Long getSuggestedQuantity() {
        return suggestedQuantity;
    }

    public void setSuggestedQuantity(Long suggestedQuantity) {
        this.suggestedQuantity = suggestedQuantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Long getPriorityRank() {
        return priorityRank;
    }

    public void setPriorityRank(Long priorityRank) {
        this.priorityRank = priorityRank;
    }
}
