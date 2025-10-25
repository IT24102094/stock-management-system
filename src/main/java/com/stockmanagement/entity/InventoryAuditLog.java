package com.stockmanagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_audit_logs")
public class InventoryAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Column(name = "action_type", nullable = false)
    private String actionType; // STOCK_INCREASE, STOCK_DECREASE

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "previous_quantity", nullable = false)
    private int previousQuantity;

    @Column(name = "new_quantity", nullable = false)
    private int newQuantity;

    @Column(name = "change_amount", nullable = false)
    private int changeAmount;

    @Column(name = "item_price")
    private BigDecimal itemPrice;

    @Column(name = "value_impact")
    private BigDecimal valueImpact;

    @Column(name = "category")
    private String category;

    @Column(name = "severity")
    private String severity; // NORMAL, MEDIUM, HIGH, CRITICAL

    @Column(name = "triggered_by")
    private String triggeredBy; // User or System action

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Constructors
    public InventoryAuditLog() {
    }

    public InventoryAuditLog(Item item, int previousQuantity, int newQuantity, 
                             int changeAmount, String actionType) {
        this.timestamp = LocalDateTime.now();
        this.item = item;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.changeAmount = changeAmount;
        this.actionType = actionType;
        this.itemPrice = item.getPrice();
        this.valueImpact = item.getPrice().multiply(new BigDecimal(Math.abs(changeAmount)));
        this.category = item.getCategory();
        this.severity = calculateSeverity(newQuantity);
    }

    private String calculateSeverity(int quantity) {
        if (quantity == 0) return "CRITICAL";
        if (quantity < 5) return "HIGH";
        if (quantity < 10) return "MEDIUM";
        return "NORMAL";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getPreviousQuantity() {
        return previousQuantity;
    }

    public void setPreviousQuantity(int previousQuantity) {
        this.previousQuantity = previousQuantity;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getValueImpact() {
        return valueImpact;
    }

    public void setValueImpact(BigDecimal valueImpact) {
        this.valueImpact = valueImpact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
