package com.stockmanagement.observer.impl;

import com.stockmanagement.entity.Item;
import com.stockmanagement.observer.StockObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer that monitors low stock levels and sends alerts
 * Observer Pattern - Concrete Observer
 */
@Component
public class LowStockAlertObserver implements StockObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(LowStockAlertObserver.class);
    private static final int LOW_STOCK_THRESHOLD = 5;
    private static final int CRITICAL_STOCK_THRESHOLD = 2;
    
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        // Check for out of stock condition
        if (newQuantity == 0 && oldQuantity > 0) {
            handleOutOfStock(item);
        }
        // Check for critical stock level
        else if (oldQuantity >= CRITICAL_STOCK_THRESHOLD && newQuantity < CRITICAL_STOCK_THRESHOLD && newQuantity > 0) {
            handleCriticalStock(item, newQuantity);
        }
        // Check for low stock condition
        else if (oldQuantity >= LOW_STOCK_THRESHOLD && newQuantity < LOW_STOCK_THRESHOLD && newQuantity > 0) {
            handleLowStock(item, newQuantity);
        }
        // Stock replenishment notification
        else if (oldQuantity < LOW_STOCK_THRESHOLD && newQuantity >= LOW_STOCK_THRESHOLD) {
            handleStockReplenishment(item, oldQuantity, newQuantity);
        }
    }
    
    /**
     * Handle out of stock situation
     */
    private void handleOutOfStock(Item item) {
        logger.error("🚨 OUT OF STOCK ALERT: {} (ID: {}) - Immediate action required!", 
                item.getName(), item.getId());
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              🚨 OUT OF STOCK ALERT 🚨                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  Item: " + String.format("%-50s", item.getName()) + "║");
        System.out.println("║  Item ID: " + String.format("%-46s", item.getId()) + "║");
        System.out.println("║  Status: STOCK DEPLETED                                    ║");
        System.out.println("║  Action: URGENT RESTOCK REQUIRED                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        // In production: Send email, SMS, push notification
        sendOutOfStockNotification(item);
    }
    
    /**
     * Handle critical stock level (< 2 units)
     */
    private void handleCriticalStock(Item item, int quantity) {
        logger.warn("⚠️ CRITICAL STOCK LEVEL: {} - Only {} units left!", item.getName(), quantity);
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│           ⚠️  CRITICAL STOCK ALERT ⚠️                 │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  Item: " + String.format("%-46s", item.getName()) + "│");
        System.out.println("│  Current Stock: " + String.format("%-38s", quantity + " units") + "│");
        System.out.println("│  Status: CRITICAL - Nearly Depleted                    │");
        System.out.println("│  Priority: HIGH                                         │");
        System.out.println("└────────────────────────────────────────────────────────┘");
        
        sendCriticalStockNotification(item, quantity);
    }
    
    /**
     * Handle low stock level (< 5 units)
     */
    private void handleLowStock(Item item, int quantity) {
        logger.warn("⚠️ LOW STOCK ALERT: {} - Quantity: {} (Below threshold: {})",
                item.getName(), quantity, LOW_STOCK_THRESHOLD);
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│              📦 LOW STOCK ALERT 📦                     │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  Item: " + String.format("%-46s", item.getName()) + "│");
        System.out.println("│  Current Stock: " + String.format("%-38s", quantity + " units") + "│");
        System.out.println("│  Threshold: " + String.format("%-42s", LOW_STOCK_THRESHOLD + " units") + "│");
        System.out.println("│  Action: Consider restocking soon                      │");
        System.out.println("└────────────────────────────────────────────────────────┘");
        
        sendLowStockNotification(item, quantity);
    }
    
    /**
     * Handle stock replenishment
     */
    private void handleStockReplenishment(Item item, int oldQuantity, int newQuantity) {
        logger.info("✅ STOCK REPLENISHED: {} - Increased from {} to {} units",
                item.getName(), oldQuantity, newQuantity);
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│           ✅ STOCK REPLENISHMENT NOTICE                │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  Item: " + String.format("%-46s", item.getName()) + "│");
        System.out.println("│  Previous: " + String.format("%-43s", oldQuantity + " units") + "│");
        System.out.println("│  Current: " + String.format("%-44s", newQuantity + " units") + "│");
        System.out.println("│  Status: Stock levels restored                          │");
        System.out.println("└────────────────────────────────────────────────────────┘");
    }
    
    private void sendOutOfStockNotification(Item item) {
        // Implementation: Send email, SMS, push notification
        logger.info("📧 Email sent to purchasing@company.com: OUT OF STOCK - {}", item.getName());
        logger.info("📱 SMS sent to manager: Item {} is OUT OF STOCK!", item.getName());
        logger.info("🔔 Push notification sent to inventory team");
    }
    
    private void sendCriticalStockNotification(Item item, int quantity) {
        logger.info("📧 Email sent to inventory@company.com: CRITICAL STOCK - {} ({} units)", 
                item.getName(), quantity);
        logger.info("📱 SMS sent to warehouse: Critical stock level for {}", item.getName());
    }
    
    private void sendLowStockNotification(Item item, int quantity) {
        logger.info("📧 Email sent to purchasing@company.com: Low stock for {} ({} units)",
                item.getName(), quantity);
        logger.info("📱 SMS sent to manager: Item {} has only {} units left", 
                item.getName(), quantity);
    }
    
    @Override
    public String getObserverName() {
        return "LowStockAlertObserver";
    }
}
