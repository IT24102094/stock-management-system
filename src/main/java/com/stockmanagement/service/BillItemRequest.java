package com.stockmanagement.service;

public class BillItemRequest {
    private Long productId;
    private Integer quantity;

    // Default constructor
    public BillItemRequest() {
        this.quantity = 0;
    }

    // Parameterized constructor
    public BillItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity != null ? quantity : 0;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity != null ? quantity : 0;
    }

    @Override
    public String toString() {
        return "BillItemRequest{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}