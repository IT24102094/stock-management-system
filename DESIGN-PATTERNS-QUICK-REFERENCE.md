# Quick Reference Guide - Design Patterns Implementation

## 🎯 Quick Access

### Factory Pattern Files
```
src/main/java/com/stockmanagement/
├── factory/
│   ├── MembershipLevel.java              [Interface]
│   ├── MembershipFactory.java            [Factory]
│   └── impl/
│       ├── StandardMembership.java       [0% discount, 1.0x points]
│       ├── SilverMembership.java         [5% discount, 1.5x points]
│       ├── GoldMembership.java           [10% discount, 2.0x points]
│       └── PlatinumMembership.java       [15% discount, 3.0x points]
└── service/
    └── CustomerService.java              [Uses Factory]
```

### Observer Pattern Files
```
src/main/java/com/stockmanagement/
├── observer/
│   ├── StockObserver.java                [Observer Interface]
│   ├── StockSubject.java                 [Subject Interface]
│   └── impl/
│       ├── LowStockAlertObserver.java    [Alerts for qty < 5]
│       ├── EmailNotificationObserver.java [Emails for changes ≥ 10]
│       ├── AuditLogObserver.java         [Logs all changes]
│       ├── DashboardUpdateObserver.java  [Updates UI stats]
│       └── AutoReorderObserver.java      [Auto PO for qty < 10]
├── service/
│   └── ItemService.java                  [Subject - notifies observers]
└── config/
    └── ObserverConfig.java               [Auto-registers observers]
```

---

## 📖 Documentation Files

| File | Description |
|------|-------------|
| `FACTORY-PATTERN-RECOMMENDATION.md` | Detailed Factory Pattern guide with code examples |
| `FACTORY-PATTERN-VISUAL-GUIDE.md` | Visual diagrams and UML for Factory Pattern |
| `OBSERVER-PATTERN-RECOMMENDATION.md` | Detailed Observer Pattern guide with code examples |
| `OBSERVER-PATTERN-VISUAL-GUIDE.md` | Visual diagrams and UML for Observer Pattern |
| `DESIGN-PATTERNS-IMPLEMENTATION-SUMMARY.md` | Complete implementation summary |
| `DESIGN-PATTERNS-ARCHITECTURE.md` | System architecture overview |
| `DESIGN-PATTERNS-QUICK-REFERENCE.md` | This quick reference guide |

---

## 🔧 How to Use

### Factory Pattern Usage

```java
// In CustomerService.java
@Autowired
private MembershipFactory membershipFactory;

public void updateMembershipLevel(Customer customer) {
    Integer points = customer.getLoyaltyPoints();
    
    // Use factory to create membership
    MembershipLevel membership = membershipFactory
        .createMembershipBySpending(points.doubleValue());
    
    // Get membership details
    String levelName = membership.getLevelName();
    double discount = membership.getDiscountPercentage();
    double multiplier = membership.getPointsMultiplier();
    
    customer.setMembershipLevel(levelName);
}
```

**Factory Methods Available:**
- `createMembership(String levelName)` - Create by name
- `createMembershipBySpending(double amount)` - Auto-determine level
- `getNextLevel(String currentLevel)` - Get upgrade level
- `canUpgrade(String level, double spending)` - Check eligibility
- `getRecommendedLevel(double spending)` - Get recommended level

### Observer Pattern Usage

```java
// In ItemService.java (already implemented)
public void updateStock(int itemId, int quantityChange) {
    // 1. Update stock
    int oldQuantity = item.getQuantity();
    int newQuantity = oldQuantity - quantityChange;
    item.setQuantity(newQuantity);
    updateItem(item);
    
    // 2. Notify all observers automatically
    notifyObservers(item, oldQuantity, newQuantity);
}
```

**Observer Methods:**
- `registerObserver(StockObserver observer)` - Add observer
- `removeObserver(StockObserver observer)` - Remove observer
- `notifyObservers(Item, oldQty, newQty)` - Notify all observers

---

## 🧪 Testing Commands

### Build Project
```bash
.\mvnw.cmd clean compile -DskipTests
```

### Run Application
```bash
.\mvnw.cmd spring-boot:run
```

### Test Factory Pattern
1. Go to: http://localhost:8080/customers/list
2. Edit customer loyalty points
3. Save and check console for membership update logs

### Test Observer Pattern
1. Create a bill or update item stock
2. Watch console for 5 observer notifications:
   - 📦 Low Stock Alert (if qty < 5)
   - 📧 Email Notification (if change ≥ 10)
   - 📝 Audit Log (always)
   - 📊 Dashboard Update (always)
   - 🔄 Auto Reorder (if qty < 10)

---

## 🎨 Console Output Examples

### Factory Pattern Output
```
═══════════════════════════════════════════════
🏆 Membership Level Updated - Factory Pattern
───────────────────────────────────────────────
Customer: John Doe
Points: 5500
New Level: Gold
Discount: 10.0%
Points Multiplier: 2.0x
Benefits: Gold Member Benefits: 10% discount...
🎯 Eligible for upgrade to: Platinum
   Points needed: 4500
═══════════════════════════════════════════════
```

### Observer Pattern Output
```
🔔 Notifying 5 observers about stock change
   Item: Laptop | Old: 15 → New: 3
───────────────────────────────────────────────────────────

┌────────────────────────────────────────────────────────┐
│              📦 LOW STOCK ALERT 📦                     │
├────────────────────────────────────────────────────────┤
│  Item: Laptop                                          │
│  Current Stock: 3 units                                │
│  Threshold: 5 units                                    │
│  Action: Consider restocking soon                      │
└────────────────────────────────────────────────────────┘

📧 EMAIL NOTIFICATION - Stock Update
   To: inventory@company.com
   Subject: Stock Update - Laptop
   Body:
   ┌─────────────────────────────────────────────
   │ Stock level decreased for: Laptop
   │ Previous Quantity: 15 units
   │ New Quantity: 3 units
   │ Change: -12 units
   └─────────────────────────────────────────────

═══════════════════════════════════════════════════════════
📝 AUDIT LOG - Stock Change Recorded
───────────────────────────────────────────────────────────
Timestamp:       2025-10-16 15:50:39
Action:          STOCK_DECREASE
Item Name:       Laptop
Previous Stock:  15 units
New Stock:       3 units
Change Amount:   -12 units
Severity:        HIGH - Low Stock
═══════════════════════════════════════════════════════════

📊 DASHBOARD UPDATE - Stock Changed
   Item: Laptop (Qty: 3)
   ┌─────────────────────────────────────────
   │ Low Stock Items:     1
   │ Out of Stock Items:  0
   │ Total Inventory Value: $...
   └─────────────────────────────────────────

🔄 WebSocket update sent to connected clients
───────────────────────────────────────────────────────────
```

---

## 🔍 Key Classes Reference

### Factory Pattern Classes

| Class | Purpose | Key Method |
|-------|---------|------------|
| `MembershipLevel` | Interface for all membership levels | `getDiscountPercentage()` |
| `MembershipFactory` | Creates membership objects | `createMembershipBySpending()` |
| `StandardMembership` | Entry level (0% discount) | All MembershipLevel methods |
| `SilverMembership` | Bronze tier (5% discount) | All MembershipLevel methods |
| `GoldMembership` | Premium tier (10% discount) | All MembershipLevel methods |
| `PlatinumMembership` | VIP tier (15% discount) | All MembershipLevel methods |

### Observer Pattern Classes

| Class | Purpose | Trigger Condition |
|-------|---------|------------------|
| `StockObserver` | Observer interface | - |
| `StockSubject` | Subject interface | - |
| `ItemService` | Subject implementation | Stock changes |
| `LowStockAlertObserver` | Low stock alerts | qty < 5 |
| `EmailNotificationObserver` | Email notifications | change ≥ 10 units |
| `AuditLogObserver` | Audit trail logging | ALL changes |
| `DashboardUpdateObserver` | Real-time UI updates | ALL changes |
| `AutoReorderObserver` | Automatic reordering | qty < 10 |

---

## 📊 Membership Level Thresholds

| Level | Points Required | Discount | Points Multiplier | Upgrade At |
|-------|----------------|----------|-------------------|------------|
| Standard | 0+ | 0% | 1.0x | 1,000 points |
| Silver | 1,000+ | 5% | 1.5x | 5,000 points |
| Gold | 5,000+ | 10% | 2.0x | 10,000 points |
| Platinum | 10,000+ | 15% | 3.0x | Max level |

## 📊 Stock Alert Thresholds

| Observer | Trigger | Action |
|----------|---------|--------|
| Low Stock Alert | qty < 5 | Console + Email + SMS (simulated) |
| Critical Stock | qty < 2 | High priority alert |
| Out of Stock | qty = 0 | Urgent notification |
| Email Notification | change ≥ 10 | Email to inventory team |
| Auto Reorder | qty < 10 | Generate purchase order |
| Audit Log | Always | Log all changes |
| Dashboard | Always | Update statistics |

---

## 🚀 Extension Examples

### Add New Membership Tier

**1. Create class:** `DiamondMembership.java`
```java
@Component
public class DiamondMembership implements MembershipLevel {
    private static final double DISCOUNT = 20.0;
    // ... implement all methods
}
```

**2. Update factory:** Add case in `MembershipFactory.createMembershipBySpending()`
```java
if (totalSpending >= 20000) {
    return new DiamondMembership();
}
```

✅ **Done!** No other changes needed.

### Add New Observer

**1. Create class:** `SmsNotificationObserver.java`
```java
@Component
public class SmsNotificationObserver implements StockObserver {
    @Override
    public void onStockChange(Item item, int old, int new) {
        if (newQuantity < 3) {
            smsService.send("+123456", "Low stock: " + item.getName());
        }
    }
    
    @Override
    public String getObserverName() {
        return "SmsNotificationObserver";
    }
}
```

**2. Register in config:** Update `ObserverConfig.java`
```java
@Autowired
private SmsNotificationObserver smsObserver;

itemService.registerObserver(smsObserver);
```

✅ **Done!** No changes to ItemService needed.

---

## 🐛 Troubleshooting

### Factory Pattern Issues

**Issue:** Membership not updating
- Check: Customer loyalty points value
- Check: Console logs for factory output
- Verify: MembershipFactory is @Autowired

**Issue:** Wrong membership level assigned
- Check: Spending thresholds in factory
- Check: Points to spending conversion logic
- Verify: Each membership class constants

### Observer Pattern Issues

**Issue:** Observers not triggered
- Check: ObserverConfig registered all observers
- Check: Console shows "Registered observer: ..."
- Verify: ItemService.updateStock() is being called

**Issue:** Only some observers working
- Check: Individual observer logic conditions
- Check: Observer exception handling
- Verify: All observers are @Component

**Issue:** Observers not registered on startup
- Check: ObserverConfig has @Configuration
- Check: CommandLineRunner bean is created
- Verify: Application logs show registration output

---

## 📞 Getting Help

### Check Logs
```bash
# Windows PowerShell
Get-Content .\logs\spring.log -Tail 50
```

### Verify Observers Registered
Look for this in console on startup:
```
╔═══════════════════════════════════════════════════════════╗
║      🔧 Registering Stock Observers (Observer Pattern)    ║
╚═══════════════════════════════════════════════════════════╝
✅ Registered observer: LowStockAlertObserver
✅ Registered observer: EmailNotificationObserver
✅ Registered observer: AuditLogObserver
✅ Registered observer: DashboardUpdateObserver
✅ Registered observer: AutoReorderObserver
```

### Test Factory Pattern
```java
// Add this temporary test endpoint
@GetMapping("/test-factory")
public String testFactory() {
    MembershipLevel gold = membershipFactory.createMembership("Gold");
    return "Level: " + gold.getLevelName() + 
           ", Discount: " + gold.getDiscountPercentage() + "%";
}
```

### Test Observer Pattern
```java
// Add this temporary test endpoint
@GetMapping("/test-observer")
public String testObserver() {
    // This will trigger all observers
    itemService.updateStock(1, 1);
    return "Check console for observer notifications";
}
```

---

## 🎓 Learning Resources

### Understand Factory Pattern
1. Read: `FACTORY-PATTERN-RECOMMENDATION.md`
2. Study: `MembershipFactory.java` implementation
3. Trace: `CustomerService.updateMembershipLevel()` execution
4. Experiment: Add a new membership tier

### Understand Observer Pattern
1. Read: `OBSERVER-PATTERN-RECOMMENDATION.md`
2. Study: `ItemService.java` implementation
3. Trace: `updateStock()` → `notifyObservers()` flow
4. Experiment: Add a new observer

### Visual Learning
- See: `FACTORY-PATTERN-VISUAL-GUIDE.md` for UML diagrams
- See: `OBSERVER-PATTERN-VISUAL-GUIDE.md` for sequence diagrams
- See: `DESIGN-PATTERNS-ARCHITECTURE.md` for system overview

---

## ✅ Checklist

### Verify Factory Pattern
- [ ] MembershipFactory is @Component
- [ ] CustomerService has @Autowired MembershipFactory
- [ ] All 4 membership classes exist
- [ ] Console shows membership updates when customer edited
- [ ] Correct discount applied based on points

### Verify Observer Pattern
- [ ] ItemService implements StockSubject
- [ ] All 5 observers are @Component
- [ ] ObserverConfig registers all observers on startup
- [ ] Console shows "Registered observer" messages
- [ ] Stock changes trigger observer notifications
- [ ] Console shows observer output (alerts, emails, logs)

---

**Quick Reference Version:** 1.0  
**Last Updated:** October 16, 2025  
**Status:** ✅ Complete and Operational
