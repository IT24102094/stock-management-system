# Observer Pattern Implementation Recommendation

## 📋 Suggested File: `ItemService.java` / `BillService.java`

### 🎯 Specific Location: Stock Update Notifications

**File Path:** `src/main/java/com/stockmanagement/service/ItemService.java`

**Current Method (Lines 76-89):**
```java
public void updateStock(int itemId, int quantityChange) {
    Optional<Item> itemOpt = getItemById(itemId);
    if (itemOpt.isPresent()) {
        Item item = itemOpt.get();
        // For purchases, quantityChange should be negative
        int newQuantity = item.getQuantity() - quantityChange;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock for item: " + item.getName());
        }
        item.setQuantity(newQuantity);
        updateItem(item);
    } else {
        throw new IllegalArgumentException("Item not found with id: " + itemId);
    }
}
```

**Also in BillService.java (Line 102):**
```java
// Update product stock
productService.updateStock(product.getId(), itemRequest.getQuantity());
```

---

## 🔍 Why Observer Pattern is Perfect Here

### 1. **Event-Driven Architecture**
When stock levels change, multiple systems need to be notified:
- **Inventory Manager** - Needs to track stock levels
- **Alert System** - Should notify when stock is low
- **Email Service** - Send alerts to purchasing department
- **Dashboard** - Update real-time statistics
- **Audit Logger** - Record stock changes
- **Analytics** - Track inventory trends
- **Reorder System** - Automatically place orders

### 2. **Current Problems**
❌ **Tight Coupling** - Stock update logic mixed with notification logic
❌ **No Extensibility** - Adding new notifications requires modifying existing code
❌ **Scattered Alerts** - LowStockAlert only prints to console
❌ **No Real-time Updates** - Manual checking required
❌ **Difficult to Test** - Cannot test notifications independently
❌ **Code Duplication** - Same alert logic in multiple places

### 3. **Business Scenarios Requiring Notifications**

#### Scenario 1: Low Stock Alert
```
Stock drops below threshold (5 units)
→ Notify: Purchasing Manager, Inventory Staff, System Admin
→ Actions: Email alert, Dashboard update, SMS notification
```

#### Scenario 2: Out of Stock
```
Stock reaches 0
→ Notify: Sales Team, Warehouse, Management
→ Actions: Block sales, Update website, Generate purchase order
```

#### Scenario 3: Stock Replenishment
```
Stock increased (new shipment arrives)
→ Notify: Sales Team, Website, Customers on waitlist
→ Actions: Enable sales, Update availability, Send customer emails
```

#### Scenario 4: Critical Stock Level
```
Fast-moving item drops below 20%
→ Notify: Procurement, Finance
→ Actions: Expedite ordering, Budget approval request
```

---

## 🏗️ Proposed Observer Pattern Implementation

### Step 1: Create Observer Interface
```java
package com.stockmanagement.observer;

import com.stockmanagement.entity.Item;

public interface StockObserver {
    /**
     * Called when stock levels change
     * @param item The item whose stock changed
     * @param oldQuantity Previous stock quantity
     * @param newQuantity New stock quantity
     */
    void onStockChange(Item item, int oldQuantity, int newQuantity);
    
    /**
     * Get observer name for logging
     * @return Observer name
     */
    String getObserverName();
}
```

### Step 2: Create Subject (Observable) Interface
```java
package com.stockmanagement.observer;

public interface StockSubject {
    /**
     * Register an observer
     * @param observer Observer to register
     */
    void registerObserver(StockObserver observer);
    
    /**
     * Remove an observer
     * @param observer Observer to remove
     */
    void removeObserver(StockObserver observer);
    
    /**
     * Notify all registered observers
     * @param item The item that changed
     * @param oldQuantity Previous quantity
     * @param newQuantity New quantity
     */
    void notifyObservers(Item item, int oldQuantity, int newQuantity);
}
```

### Step 3: Create Concrete Observers

#### Observer 1: Low Stock Alert Observer
```java
package com.stockmanagement.observer.impl;

import com.stockmanagement.entity.Item;
import com.stockmanagement.observer.StockObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LowStockAlertObserver implements StockObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(LowStockAlertObserver.class);
    private static final int LOW_STOCK_THRESHOLD = 5;
    
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        // Alert when crossing threshold
        if (oldQuantity >= LOW_STOCK_THRESHOLD && newQuantity < LOW_STOCK_THRESHOLD) {
            logger.warn("⚠️ LOW STOCK ALERT: {} - Quantity: {} (Below threshold: {})",
                    item.getName(), newQuantity, LOW_STOCK_THRESHOLD);
            
            // Send notification
            sendLowStockNotification(item, newQuantity);
        }
        
        // Alert when out of stock
        if (newQuantity == 0 && oldQuantity > 0) {
            logger.error("🚨 OUT OF STOCK: {} - Immediate action required!", item.getName());
            sendOutOfStockNotification(item);
        }
    }
    
    private void sendLowStockNotification(Item item, int quantity) {
        // Implementation: Send email, SMS, push notification
        System.out.println("📧 Email sent to purchasing@company.com: Low stock for " + item.getName());
        System.out.println("📱 SMS sent to manager: Item " + item.getName() + " has only " + quantity + " units left");
    }
    
    private void sendOutOfStockNotification(Item item) {
        System.out.println("🚨 URGENT: " + item.getName() + " is OUT OF STOCK!");
    }
    
    @Override
    public String getObserverName() {
        return "LowStockAlertObserver";
    }
}
```

#### Observer 2: Email Notification Observer
```java
package com.stockmanagement.observer.impl;

import com.stockmanagement.entity.Item;
import com.stockmanagement.observer.StockObserver;
import com.stockmanagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements StockObserver {
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        // Send email for significant stock changes
        if (Math.abs(newQuantity - oldQuantity) >= 10) {
            String subject = "Stock Update: " + item.getName();
            String body = String.format(
                "Stock level changed for item: %s\n" +
                "Previous Quantity: %d\n" +
                "New Quantity: %d\n" +
                "Change: %d\n",
                item.getName(), oldQuantity, newQuantity, (newQuantity - oldQuantity)
            );
            
            // emailService.sendEmail("inventory@company.com", subject, body);
            System.out.println("📧 Email Notification: " + subject);
        }
    }
    
    @Override
    public String getObserverName() {
        return "EmailNotificationObserver";
    }
}
```

#### Observer 3: Audit Log Observer
```java
package com.stockmanagement.observer.impl;

import com.stockmanagement.entity.Item;
import com.stockmanagement.observer.StockObserver;
import com.stockmanagement.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditLogObserver implements StockObserver {
    
    @Autowired
    private AuditService auditService;
    
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        // Log every stock change to audit trail
        String logMessage = String.format(
            "Stock changed for Item ID: %d, Name: %s, Old: %d, New: %d",
            item.getId(), item.getName(), oldQuantity, newQuantity
        );
        
        // auditService.logAction("STOCK_UPDATE", logMessage);
        System.out.println("📝 Audit Log: " + logMessage);
    }
    
    @Override
    public String getObserverName() {
        return "AuditLogObserver";
    }
}
```

#### Observer 4: Dashboard Update Observer
```java
package com.stockmanagement.observer.impl;

import com.stockmanagement.entity.Item;
import com.stockmanagement.observer.StockObserver;
import org.springframework.stereotype.Component;

@Component
public class DashboardUpdateObserver implements StockObserver {
    
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        // Update real-time dashboard statistics
        updateLowStockCount(newQuantity);
        updateInventoryValue(item, oldQuantity, newQuantity);
        
        System.out.println("📊 Dashboard Updated: " + item.getName() + " stock changed");
    }
    
    private void updateLowStockCount(int newQuantity) {
        // Update count of low-stock items
        if (newQuantity < 5) {
            // Increment low stock counter
        }
    }
    
    private void updateInventoryValue(Item item, int oldQuantity, int newQuantity) {
        // Recalculate total inventory value
        double valueDiff = (newQuantity - oldQuantity) * item.getPrice();
        // Update total inventory value
    }
    
    @Override
    public String getObserverName() {
        return "DashboardUpdateObserver";
    }
}
```

#### Observer 5: Auto-Reorder Observer
```java
package com.stockmanagement.observer.impl;

import com.stockmanagement.entity.Item;
import com.stockmanagement.observer.StockObserver;
import org.springframework.stereotype.Component;

@Component
public class AutoReorderObserver implements StockObserver {
    
    private static final int REORDER_POINT = 10;
    
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        // Trigger automatic reorder when below reorder point
        if (oldQuantity >= REORDER_POINT && newQuantity < REORDER_POINT) {
            triggerAutoReorder(item, newQuantity);
        }
    }
    
    private void triggerAutoReorder(Item item, int currentQuantity) {
        int reorderQuantity = 50; // Order standard quantity
        System.out.println("🔄 Auto-Reorder Triggered: " + item.getName());
        System.out.println("   Current: " + currentQuantity + " units");
        System.out.println("   Ordering: " + reorderQuantity + " units");
        
        // Create purchase order automatically
        // purchaseOrderService.createAutoOrder(item, reorderQuantity);
    }
    
    @Override
    public String getObserverName() {
        return "AutoReorderObserver";
    }
}
```

### Step 4: Implement Subject in ItemService
```java
package com.stockmanagement.service;

import com.stockmanagement.entity.Item;
import com.stockmanagement.observer.StockObserver;
import com.stockmanagement.observer.StockSubject;
import com.stockmanagement.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService implements StockSubject {

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private ViewItemService viewItemService;
    
    @Autowired
    private AddItemService addItemService;
    
    @Autowired
    private UpdateItemService updateItemService;
    
    @Autowired
    private DeleteItemService deleteItemService;
    
    // Observer Pattern: List of observers
    private final List<StockObserver> observers = new ArrayList<>();

    // ===== OBSERVER PATTERN METHODS =====
    
    @Override
    public void registerObserver(StockObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✅ Registered observer: " + observer.getObserverName());
        }
    }
    
    @Override
    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
        System.out.println("❌ Removed observer: " + observer.getObserverName());
    }
    
    @Override
    public void notifyObservers(Item item, int oldQuantity, int newQuantity) {
        System.out.println("🔔 Notifying " + observers.size() + " observers about stock change");
        for (StockObserver observer : observers) {
            try {
                observer.onStockChange(item, oldQuantity, newQuantity);
            } catch (Exception e) {
                System.err.println("Error in observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }

    // ===== EXISTING METHODS (REFACTORED) =====
    
    /**
     * Get all items
     */
    public List<Item> getAllItems() {
        return viewItemService.getAllItems();
    }

    /**
     * Get item by ID
     */
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }
    
    /**
     * Get item by ID (backward compatibility)
     */
    public Optional<Item> getItemById(int id) {
        return getItemById((long) id);
    }
    
    /**
     * Add a new item
     */
    public Item addItem(Item item) {
        addItemService.addItem(item);
        return item;
    }
    
    /**
     * Update an existing item
     */
    public Item updateItem(Item item) {
        updateItemService.updateItem(item);
        return item;
    }
    
    /**
     * Delete an item
     */
    public void deleteItem(int id) {
        deleteItemService.deleteItem(id);
    }
    
    /**
     * Update stock quantity (REFACTORED WITH OBSERVER PATTERN)
     */
    public void updateStock(int itemId, int quantityChange) {
        Optional<Item> itemOpt = getItemById(itemId);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            
            // Store old quantity for observers
            int oldQuantity = item.getQuantity();
            
            // Calculate new quantity
            int newQuantity = item.getQuantity() - quantityChange;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Insufficient stock for item: " + item.getName());
            }
            
            // Update the quantity
            item.setQuantity(newQuantity);
            updateItem(item);
            
            // 🔔 NOTIFY ALL OBSERVERS ABOUT THE CHANGE
            notifyObservers(item, oldQuantity, newQuantity);
            
        } else {
            throw new IllegalArgumentException("Item not found with id: " + itemId);
        }
    }
}
```

### Step 5: Auto-Register Observers on Startup
```java
package com.stockmanagement.config;

import com.stockmanagement.observer.impl.*;
import com.stockmanagement.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private LowStockAlertObserver lowStockAlertObserver;
    
    @Autowired
    private EmailNotificationObserver emailNotificationObserver;
    
    @Autowired
    private AuditLogObserver auditLogObserver;
    
    @Autowired
    private DashboardUpdateObserver dashboardUpdateObserver;
    
    @Autowired
    private AutoReorderObserver autoReorderObserver;
    
    @Bean
    public CommandLineRunner registerObservers() {
        return args -> {
            System.out.println("🔧 Registering Stock Observers...");
            
            itemService.registerObserver(lowStockAlertObserver);
            itemService.registerObserver(emailNotificationObserver);
            itemService.registerObserver(auditLogObserver);
            itemService.registerObserver(dashboardUpdateObserver);
            itemService.registerObserver(autoReorderObserver);
            
            System.out.println("✅ All observers registered successfully!");
        };
    }
}
```

---

## ✅ Benefits of This Implementation

### 1. **Loose Coupling**
✅ ItemService doesn't know about specific observers
✅ Easy to add/remove observers without changing core logic
✅ Observers are independent of each other

### 2. **Open/Closed Principle**
✅ Open for extension (add new observers)
✅ Closed for modification (no changes to ItemService)

### 3. **Single Responsibility**
✅ ItemService: Manages stock
✅ Observers: Handle specific notifications
✅ Each observer has one responsibility

### 4. **Scalability**
✅ Add unlimited observers
✅ Enable/disable observers at runtime
✅ Priority-based notification ordering

### 5. **Testability**
```java
@Test
public void testLowStockObserver() {
    LowStockAlertObserver observer = new LowStockAlertObserver();
    Item item = new Item();
    item.setName("Test Item");
    
    observer.onStockChange(item, 10, 3);
    // Verify alert was triggered
}
```

---

## 📊 Comparison: Before vs After

### Before (Current Code)
```
❌ Stock update logic mixed with alerts
❌ Only console logging
❌ Hard to add new notifications
❌ Tight coupling
❌ Difficult to test
```

### After (Observer Pattern)
```
✅ Clean separation of concerns
✅ Multiple notification channels (email, SMS, dashboard, etc.)
✅ Add observers with zero code changes
✅ Loose coupling
✅ Easy unit testing
✅ Real-time notifications
```

---

## 🎯 Real-World Usage Example

### Scenario: Product Purchase

```java
// In BillService.java
productService.updateStock(product.getId(), itemRequest.getQuantity());

// What happens internally:
// 1. ItemService updates the stock quantity
// 2. ItemService notifies all registered observers
// 3. Each observer reacts independently:
//    - LowStockAlertObserver: Checks if below threshold → sends alert
//    - EmailNotificationObserver: Sends email to purchasing department
//    - AuditLogObserver: Logs the stock change
//    - DashboardUpdateObserver: Updates real-time dashboard
//    - AutoReorderObserver: Triggers automatic reorder if needed
```

### Console Output:
```
🔔 Notifying 5 observers about stock change
✅ Registered observer: LowStockAlertObserver
⚠️ LOW STOCK ALERT: Laptop - Quantity: 3 (Below threshold: 5)
📧 Email sent to purchasing@company.com: Low stock for Laptop
📱 SMS sent to manager: Item Laptop has only 3 units left
✅ Registered observer: EmailNotificationObserver
📧 Email Notification: Stock Update: Laptop
✅ Registered observer: AuditLogObserver
📝 Audit Log: Stock changed for Item ID: 1, Name: Laptop, Old: 15, New: 3
✅ Registered observer: DashboardUpdateObserver
📊 Dashboard Updated: Laptop stock changed
✅ Registered observer: AutoReorderObserver
```

---

## 🚀 Future Enhancements

Once Observer Pattern is implemented:

### 1. **SMS Notifications**
```java
@Component
public class SmsNotificationObserver implements StockObserver {
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        if (newQuantity < 5) {
            smsService.send("+1234567890", "Low stock: " + item.getName());
        }
    }
}
```

### 2. **Slack/Teams Integration**
```java
@Component
public class SlackNotificationObserver implements StockObserver {
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        slackService.postMessage("#inventory", 
            "Stock alert: " + item.getName() + " - " + newQuantity + " units left");
    }
}
```

### 3. **Customer Waitlist Notifications**
```java
@Component
public class WaitlistNotificationObserver implements StockObserver {
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        if (oldQuantity == 0 && newQuantity > 0) {
            // Item back in stock - notify waiting customers
            waitlistService.notifyCustomers(item);
        }
    }
}
```

### 4. **Analytics & Reporting**
```java
@Component
public class AnalyticsObserver implements StockObserver {
    @Override
    public void onStockChange(Item item, int oldQuantity, int newQuantity) {
        // Track stock velocity
        // Predict future stock needs
        // Generate analytics reports
    }
}
```

---

## 📝 Summary

**Files to Modify/Create:**
1. **Create:** `StockObserver.java` (interface)
2. **Create:** `StockSubject.java` (interface)
3. **Create:** `LowStockAlertObserver.java`
4. **Create:** `EmailNotificationObserver.java`
5. **Create:** `AuditLogObserver.java`
6. **Create:** `DashboardUpdateObserver.java`
7. **Create:** `AutoReorderObserver.java`
8. **Create:** `ObserverConfig.java`
9. **Modify:** `ItemService.java` (implement StockSubject)
10. **Deprecate:** `LowStockAlert.java` (replaced by observer)

**Pattern:** Observer Pattern (Behavioral Design Pattern)

**Justification:**
- ✅ Event-driven architecture
- ✅ Decouples stock management from notifications
- ✅ Extensible notification system
- ✅ Real-time updates
- ✅ Follows SOLID principles
- ✅ Industry-standard pattern for this use case

This is a **perfect real-world example** of Observer Pattern! 🌟
