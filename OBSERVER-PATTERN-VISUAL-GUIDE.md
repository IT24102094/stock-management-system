# Observer Pattern - Visual Implementation Guide
## Stock Management System - Event-Driven Notifications

---

## 🎯 Pattern Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     OBSERVER PATTERN                         │
│                                                               │
│  When stock changes → Notify all interested parties          │
│  One event → Multiple independent reactions                  │
│  Decoupled notification system                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 📐 UML Class Diagram

```
┌──────────────────────────────┐
│      <<interface>>           │
│      StockSubject            │
├──────────────────────────────┤
│ + registerObserver()         │
│ + removeObserver()           │
│ + notifyObservers()          │
└──────────────────────────────┘
             △
             │ implements
             │
┌────────────┴─────────────┐
│      ItemService         │
├──────────────────────────┤
│ - observers: List        │
├──────────────────────────┤
│ + updateStock()          │───────┐
│ + registerObserver()     │       │ notifies
│ + removeObserver()       │       │
│ + notifyObservers()      │       │
└──────────────────────────┘       │
                                    │
                                    ▼
┌────────────────────────────────────────────────────────┐
│              <<interface>>                             │
│              StockObserver                             │
├────────────────────────────────────────────────────────┤
│ + onStockChange(item, oldQty, newQty)                 │
│ + getObserverName()                                    │
└────────────────────────────────────────────────────────┘
                         △
                         │ implements
         ┌───────────────┼───────────────┬──────────────┐
         │               │               │              │
┌────────┴────────┐ ┌────┴───────┐ ┌────┴────────┐ ┌──┴─────────┐
│ LowStockAlert   │ │EmailNotif  │ │AuditLog     │ │Dashboard   │
│ Observer        │ │Observer    │ │Observer     │ │Observer    │
├─────────────────┤ ├────────────┤ ├─────────────┤ ├────────────┤
│ + onStockChange │ │+ onStock   │ │+ onStock    │ │+ onStock   │
│                 │ │  Change    │ │  Change     │ │  Change    │
└─────────────────┘ └────────────┘ └─────────────┘ └────────────┘
```

---

## 🔄 Sequence Diagram: Stock Update Flow

```
User            BillService    ItemService    LowStockObserver  EmailObserver  AuditObserver
 │                  │              │                 │               │              │
 │  Purchase Item   │              │                 │               │              │
 ├─────────────────>│              │                 │               │              │
 │                  │              │                 │               │              │
 │                  │ updateStock()│                 │               │              │
 │                  ├─────────────>│                 │               │              │
 │                  │              │                 │               │              │
 │                  │              │ Update DB       │               │              │
 │                  │              │ (15 → 3)        │               │              │
 │                  │              │                 │               │              │
 │                  │              │ notifyObservers()              │              │
 │                  │              ├─────────────────┼───────────────┼─────────────>│
 │                  │              │                 │               │              │
 │                  │              │  onStockChange()│               │              │
 │                  │              ├────────────────>│               │              │
 │                  │              │                 │               │              │
 │                  │              │                 │ Check: 3 < 5? │              │
 │                  │              │                 │ ✅ YES         │              │
 │                  │              │                 │               │              │
 │                  │              │                 │ 📧 Send Alert  │              │
 │                  │              │                 │ 📱 Send SMS    │              │
 │                  │              │                 │               │              │
 │                  │              │                 onStockChange() │              │
 │                  │              ├─────────────────────────────────>│              │
 │                  │              │                 │               │              │
 │                  │              │                 │ 📧 Email Sent  │              │
 │                  │              │                 │               │              │
 │                  │              │                                 onStockChange()│
 │                  │              ├─────────────────────────────────────────────────>│
 │                  │              │                 │               │              │
 │                  │              │                 │               │ 📝 Log Change│
 │                  │              │                 │               │              │
 │                  │    Return    │                 │               │              │
 │                  │<─────────────┤                 │               │              │
 │   Confirmation   │              │                 │               │              │
 │<─────────────────┤              │                 │               │              │
```

---

## 🏗️ Component Structure

```
┌─────────────────────────────────────────────────────────────────┐
│                          ItemService                             │
│                    (Subject/Observable)                          │
│                                                                   │
│  observers = [Observer1, Observer2, Observer3, ...]             │
│                                                                   │
│  updateStock(itemId, quantity) {                                │
│      oldQty = item.getQuantity()                                │
│      newQty = oldQty - quantity                                 │
│      item.setQuantity(newQty)                                   │
│      save(item)                                                 │
│      notifyObservers(item, oldQty, newQty)  ◄─── Event Trigger │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                               │
                               │ notifies
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Observer Pipeline                           │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │  Observer 1  │  │  Observer 2  │  │  Observer 3  │          │
│  │ Low Stock    │  │    Email     │  │  Audit Log   │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│         │                  │                  │                  │
│         ▼                  ▼                  ▼                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │ Check if     │  │ Send email   │  │ Log to DB    │          │
│  │ qty < 5      │  │ to manager   │  │ & file       │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│         │                  │                  │                  │
│         ▼                  ▼                  ▼                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │ 📱 Alert     │  │ 📧 Email     │  │ 📝 Record    │          │
│  │   Sent       │  │   Sent       │  │   Created    │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎬 Execution Flow

### Before Observer Pattern ❌

```
┌─────────────────────────────────────────────────────┐
│             ItemService.updateStock()                │
├─────────────────────────────────────────────────────┤
│                                                       │
│  1. Validate stock                                   │
│  2. Update quantity                                  │
│  3. Save to database                                 │
│  4. Check if low stock ◄─── Tightly coupled         │
│  5. Print console alert ◄─── Limited notification   │
│  6. (Want email?) ◄─── Must modify this method      │
│  7. (Want SMS?) ◄─── Must modify this method        │
│  8. (Want audit log?) ◄─── Must modify this method  │
│                                                       │
│  ❌ Violates Open/Closed Principle                   │
│  ❌ Violates Single Responsibility                   │
│  ❌ Hard to test                                      │
│  ❌ Tight coupling                                    │
└─────────────────────────────────────────────────────┘
```

### After Observer Pattern ✅

```
┌─────────────────────────────────────────────────────┐
│             ItemService.updateStock()                │
├─────────────────────────────────────────────────────┤
│                                                       │
│  1. Validate stock                                   │
│  2. Update quantity                                  │
│  3. Save to database                                 │
│  4. notifyObservers(item, oldQty, newQty) ◄── Clean │
│                                                       │
│  ✅ Single responsibility                            │
│  ✅ Open for extension                               │
│  ✅ Easy to test                                      │
│  ✅ Loose coupling                                    │
└─────────────────────────────────────────────────────┘
                       │
                       ├──► LowStockAlertObserver
                       │     → Checks threshold
                       │     → Sends alerts
                       │
                       ├──► EmailNotificationObserver
                       │     → Sends emails
                       │
                       ├──► AuditLogObserver
                       │     → Records changes
                       │
                       ├──► DashboardUpdateObserver
                       │     → Updates UI
                       │
                       └──► AutoReorderObserver
                             → Triggers purchase orders
```

---

## 📊 Before & After Code Comparison

### ❌ BEFORE: Tightly Coupled

```java
// ItemService.java (BEFORE)
public void updateStock(int itemId, int quantityChange) {
    Item item = getItemById(itemId);
    int newQuantity = item.getQuantity() - quantityChange;
    item.setQuantity(newQuantity);
    updateItem(item);
    
    // ❌ PROBLEM: Notification logic mixed with business logic
    if (newQuantity < 5) {
        System.out.println("LOW STOCK: " + item.getName());
    }
    
    // Want to add email? Must modify this method!
    // Want to add SMS? Must modify this method!
    // Want to add audit log? Must modify this method!
}
```

### ✅ AFTER: Observer Pattern

```java
// ItemService.java (AFTER)
public void updateStock(int itemId, int quantityChange) {
    Item item = getItemById(itemId);
    int oldQuantity = item.getQuantity();
    int newQuantity = oldQuantity - quantityChange;
    item.setQuantity(newQuantity);
    updateItem(item);
    
    // ✅ SOLUTION: Just notify observers
    notifyObservers(item, oldQuantity, newQuantity);
    
    // Want to add email? Just create EmailObserver!
    // Want to add SMS? Just create SmsObserver!
    // Want to add audit log? Just create AuditObserver!
    // NO CHANGES to this method!
}
```

---

## 🎯 Observer Registration Flow

```
Application Startup
        │
        ▼
┌─────────────────────────────────────────┐
│      ObserverConfig.java                │
│   (Spring Configuration)                 │
│                                          │
│  @Bean                                   │
│  CommandLineRunner registerObservers() { │
│      itemService.registerObserver(       │
│          lowStockObserver               │
│      )                                   │
│      itemService.registerObserver(       │
│          emailObserver                  │
│      )                                   │
│      itemService.registerObserver(       │
│          auditObserver                  │
│      )                                   │
│  }                                       │
└─────────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────────┐
│         ItemService                      │
│                                          │
│  observers = []                         │
│  registerObserver(observer1) → [obs1]   │
│  registerObserver(observer2) → [obs1,2] │
│  registerObserver(observer3) → [obs1,2,3]│
└─────────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────────┐
│      Application Ready                   │
│                                          │
│  ✅ All observers registered             │
│  ✅ System listening for stock changes   │
└─────────────────────────────────────────┘
```

---

## 🔔 Notification Chain Example

### Scenario: Stock drops from 15 to 3 units

```
Stock Update Event
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  ItemService.notifyObservers(item, 15, 3)                │
└──────────────────────────────────────────────────────────┘
       │
       │  Parallel Execution
       │
       ├─────────────────────────┬─────────────────────┬────────────────┐
       │                         │                     │                │
       ▼                         ▼                     ▼                ▼
┌──────────────┐    ┌──────────────────┐   ┌────────────────┐  ┌────────────┐
│LowStockAlert │    │EmailNotification │   │AuditLog        │  │Dashboard   │
│Observer      │    │Observer          │   │Observer        │  │Observer    │
└──────────────┘    └──────────────────┘   └────────────────┘  └────────────┘
       │                     │                      │                 │
       ▼                     ▼                      ▼                 ▼
┌──────────────┐    ┌──────────────────┐   ┌────────────────┐  ┌────────────┐
│Check: 3 < 5? │    │Change > 10?      │   │Log to DB       │  │Update      │
│✅ YES        │    │15-3=12, ✅ YES   │   │                │  │Stats       │
└──────────────┘    └──────────────────┘   └────────────────┘  └────────────┘
       │                     │                      │                 │
       ▼                     ▼                      ▼                 ▼
┌──────────────┐    ┌──────────────────┐   ┌────────────────┐  ┌────────────┐
│📱 SMS Alert  │    │📧 Email to       │   │📝 "Stock       │  │📊 Low      │
│📧 Email      │    │  purchasing@     │   │changed from    │  │Stock Count │
│🔔 Push       │    │  company.com"    │   │15 to 3"        │  │+1          │
└──────────────┘    └──────────────────┘   └────────────────┘  └────────────┘

Result: Multiple independent reactions to one event!
```

---

## 📦 Package Structure

```
src/main/java/com/stockmanagement/
│
├── observer/
│   ├── StockObserver.java                    (Interface)
│   ├── StockSubject.java                     (Interface)
│   │
│   └── impl/
│       ├── LowStockAlertObserver.java       (Concrete Observer)
│       ├── EmailNotificationObserver.java   (Concrete Observer)
│       ├── AuditLogObserver.java            (Concrete Observer)
│       ├── DashboardUpdateObserver.java     (Concrete Observer)
│       └── AutoReorderObserver.java         (Concrete Observer)
│
├── service/
│   └── ItemService.java                      (Subject - implements StockSubject)
│
└── config/
    └── ObserverConfig.java                   (Spring Configuration)
```

---

## 🧪 Testing Example

```
Test Case: Low Stock Alert
        │
        ▼
┌─────────────────────────────────────────────────────────┐
│  @Test                                                   │
│  void testLowStockObserver() {                          │
│      // Arrange                                          │
│      LowStockAlertObserver observer = new ...;          │
│      Item item = createTestItem("Laptop", 10);          │
│                                                          │
│      // Act                                              │
│      observer.onStockChange(item, 10, 3);               │
│                                                          │
│      // Assert                                           │
│      verify(alertService).sendAlert(...);               │
│  }                                                       │
└─────────────────────────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────┐
│  ✅ Test passes - Alert sent when stock < 5             │
│  ✅ Independent of ItemService                          │
│  ✅ No database required                                │
│  ✅ Fast unit test                                      │
└─────────────────────────────────────────────────────────┘
```

---

## 🌟 Real-World Analogy

```
┌────────────────────────────────────────────────────────────┐
│                     YouTube Channel                         │
│                  (Subject/Observable)                       │
│                                                              │
│              "New video uploaded!"                          │
└────────────────────────────────────────────────────────────┘
                          │
                          │ notifies
                          ▼
┌────────────────────────────────────────────────────────────┐
│                     Subscribers                             │
│                     (Observers)                             │
│                                                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │ Subscriber│  │Subscriber│  │Subscriber│  │Subscriber│  │
│  │     1     │  │    2     │  │    3     │  │    4     │  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  │
│       │             │              │              │         │
│       ▼             ▼              ▼              ▼         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │📧 Email  │  │🔔 Push   │  │📱 SMS    │  │💬 App    │  │
│  │ Notif    │  │ Notif    │  │ Notif    │  │ Notif    │  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  │
└────────────────────────────────────────────────────────────┘

Similarly:
Stock Update (Subject) → Notifies → Observers (Alerts, Emails, Logs)
```

---

## 🚀 Extensibility Example

### Adding New Observer (Zero Changes to Existing Code!)

```
Step 1: Create new observer
┌─────────────────────────────────────────┐
│  SmsNotificationObserver.java           │
│                                          │
│  implements StockObserver {             │
│      onStockChange(...) {               │
│          if (newQty < 5) {              │
│              smsService.send(...)       │
│          }                               │
│      }                                   │
│  }                                       │
└─────────────────────────────────────────┘

Step 2: Register the observer
┌─────────────────────────────────────────┐
│  ObserverConfig.java                     │
│                                          │
│  itemService.registerObserver(          │
│      smsNotificationObserver            │
│  );                                      │
└─────────────────────────────────────────┘

Step 3: Done! ✅
┌─────────────────────────────────────────┐
│  No changes to:                          │
│  ✅ ItemService.java                     │
│  ✅ Other observers                      │
│  ✅ Business logic                       │
│  ✅ Database                             │
└─────────────────────────────────────────┘
```

---

## 📈 Scalability Visualization

```
                        Current System
                        ──────────────
                              │
                              ▼
                    ┌──────────────────┐
                    │   ItemService    │
                    │   updateStock()  │
                    └──────────────────┘
                              │
                              │ Notify
                              ▼
                    ┌──────────────────┐
                    │   3 Observers    │
                    └──────────────────┘


                     Adding 10 More Observers
                     ────────────────────────
                              │
                              ▼
                    ┌──────────────────┐
                    │   ItemService    │
                    │   updateStock()  │  ◄── NO CHANGES!
                    └──────────────────┘
                              │
                              │ Notify
                              ▼
                    ┌──────────────────┐
                    │  13 Observers    │  ◄── Just add more!
                    └──────────────────┘


                     Scalability = ∞
```

---

## ✅ SOLID Principles Adherence

```
┌──────────────────────────────────────────────────────────┐
│  S - Single Responsibility Principle                     │
│  ✅ ItemService: Manages stock                           │
│  ✅ LowStockObserver: Sends low stock alerts            │
│  ✅ EmailObserver: Sends emails                         │
│  ✅ Each class has ONE responsibility                   │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  O - Open/Closed Principle                               │
│  ✅ Open for extension: Add new observers               │
│  ✅ Closed for modification: No changes to ItemService  │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  L - Liskov Substitution Principle                       │
│  ✅ Any StockObserver can replace another               │
│  ✅ Polymorphic behavior                                │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  I - Interface Segregation Principle                     │
│  ✅ Small, focused interfaces                           │
│  ✅ Observers only implement what they need             │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  D - Dependency Inversion Principle                      │
│  ✅ ItemService depends on StockObserver interface      │
│  ✅ Not on concrete implementations                     │
└──────────────────────────────────────────────────────────┘
```

---

## 🎓 Summary

**Pattern:** Observer (Behavioral Design Pattern)

**Problem Solved:** Decoupling stock updates from notifications

**Key Benefit:** Add unlimited observers without changing core business logic

**Real-World Use:** YouTube subscriptions, Event listeners, Notification systems

**Perfect For:** 
- Event-driven architectures
- Real-time notifications
- Loosely coupled systems
- Scalable applications

---

## 🏆 Why This Pattern Rocks

```
┌─────────────────────────────────────────────────────────┐
│                                                          │
│  "When one object changes state, all dependent          │
│   objects are notified and updated automatically."      │
│                                                          │
│               - Gang of Four                             │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

**Perfect fit for your stock management system!** 🌟

