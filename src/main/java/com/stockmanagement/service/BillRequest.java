package com.stockmanagement.service;

import java.util.ArrayList;
import java.util.List;

// Complete BillRequest DTO with Getters and Setters
public class BillRequest {
    private Long customerId;
    private List<BillItemRequest> items;
    private String notes;

    // Default constructor
    public BillRequest() {
        this.items = new ArrayList<>();
    }

    // Parameterized constructor
    public BillRequest(Long customerId, List<BillItemRequest> items, String notes) {
        this.customerId = customerId;
        this.items = items != null ? items : new ArrayList<>();
        this.notes = notes;
    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<BillItemRequest> getItems() {
        return items;
    }

    public void setItems(List<BillItemRequest> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
    
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper methods
    public void addItem(BillItemRequest item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    public void removeItem(BillItemRequest item) {
        if (this.items != null) {
            this.items.remove(item);
        }
    }

    public void clearItems() {
        if (this.items != null) {
            this.items.clear();
        }
    }
    
    // Helper method to convert from DTO
    public static BillRequest fromDTO(com.stockmanagement.dto.BillRequest dto) {
        BillRequest request = new BillRequest();
        request.setCustomerId(dto.getCustomerId());
        request.setNotes(dto.getNotes());
        
        for (com.stockmanagement.dto.BillItemRequest itemDto : dto.getItems()) {
            BillItemRequest item = new BillItemRequest();
            item.setProductId(itemDto.getProductId());
            item.setQuantity(itemDto.getQuantity());
            request.addItem(item);
        }
        
        return request;
    }

    @Override
    public String toString() {
        return "BillRequest{" +
                "customerId=" + customerId +
                ", items=" + items +
                ", notes='" + notes + '\'' +
                '}';
    }
}
