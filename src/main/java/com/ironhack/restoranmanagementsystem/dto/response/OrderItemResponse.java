package com.ironhack.restoranmanagementsystem.dto.response;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private Long productId;
    private String productName;

    public OrderItemResponse() {}

    public OrderItemResponse(Long id, int quantity, BigDecimal unitPrice,
                             Long productId, String productName) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productId = productId;
        this.productName = productName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}