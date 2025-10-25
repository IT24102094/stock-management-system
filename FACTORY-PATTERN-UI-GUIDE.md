# Factory Pattern - Complete Guide & UI Identification

## 📚 What is Factory Pattern?

The **Factory Pattern** is a creational design pattern that provides an interface for creating objects without specifying their exact class. Instead of calling `new` directly, you ask a "factory" to create the object for you.

### Why Use Factory Pattern?
✅ **Encapsulation** - Hide object creation complexity  
✅ **Flexibility** - Easy to add new types without changing existing code  
✅ **Consistency** - Centralized creation logic  
✅ **Maintainability** - Changes in one place affect all  

---

## 🏭 Your Factory Pattern Implementation

### **Use Case: Customer Membership Levels**

Your system uses the Factory Pattern to create different membership levels (Standard, Silver, Gold, Platinum) with varying benefits.

---

## 📋 Factory Pattern Components

### 1️⃣ **Product Interface** (`MembershipLevel.java`)

```java
public interface MembershipLevel {
    String getLevelName();                    // "Standard", "Silver", "Gold", "Platinum"
    double getDiscountPercentage();           // 0%, 5%, 10%, 15%
    double getPointsMultiplier();             // 1.0x, 1.5x, 2.0x, 3.0x
    double getMinimumSpending();              // $0, $1000, $5000, $10000
    boolean isEligibleForUpgrade(double totalSpending);
    String getNextLevelName();
    String getBenefitsDescription();
}
```

**Purpose:** Defines the contract that all membership types must follow.

---

### 2️⃣ **Concrete Products** (Implementation Classes)

#### **StandardMembership.java**
```java
public class StandardMembership implements MembershipLevel {
    private static final double DISCOUNT_PERCENTAGE = 0.0;    // No discount
    private static final double POINTS_MULTIPLIER = 1.0;      // 1x points
    private static final double MINIMUM_SPENDING = 0.0;       // No minimum
    private static final double UPGRADE_THRESHOLD = 1000.0;   // $1,000 to upgrade
    
    @Override
    public String getLevelName() { return "Standard"; }
    
    @Override
    public String getBenefitsDescription() {
        return "Welcome to our store! Start earning points with every purchase. " +
               "Spend $1,000+ to unlock Silver membership benefits.";
    }
}
```

#### **SilverMembership.java**
- 5% discount
- 1.5x points multiplier
- $1,000 minimum spending
- Upgrade at $5,000

#### **GoldMembership.java**
- 10% discount
- 2.0x points multiplier
- $5,000 minimum spending
- Upgrade at $10,000

#### **PlatinumMembership.java**
- 15% discount
- 3.0x points multiplier
- $10,000 minimum spending
- Maximum level (no upgrade)

---

### 3️⃣ **Factory Class** (`MembershipFactory.java`)

```java
@Component
public class MembershipFactory {
    
    // Method 1: Create by level name
    public MembershipLevel createMembership(String levelName) {
        switch (levelName.toLowerCase()) {
            case "standard": return new StandardMembership();
            case "silver":   return new SilverMembership();
            case "gold":     return new GoldMembership();
            case "platinum": return new PlatinumMembership();
            default:
                throw new IllegalArgumentException("Invalid level: " + levelName);
        }
    }
    
    // Method 2: Create by spending amount
    public MembershipLevel createMembershipBySpending(double totalSpending) {
        if (totalSpending >= 10000)      return new PlatinumMembership();
        else if (totalSpending >= 5000)  return new GoldMembership();
        else if (totalSpending >= 1000)  return new SilverMembership();
        else                             return new StandardMembership();
    }
    
    // Method 3: Get next level
    public MembershipLevel getNextLevel(String currentLevel) {
        MembershipLevel current = createMembership(currentLevel);
        String nextLevelName = current.getNextLevelName();
        return createMembership(nextLevelName);
    }
    
    // Method 4: Check if upgrade available
    public boolean canUpgrade(String currentLevel, double totalSpending) {
        MembershipLevel membership = createMembership(currentLevel);
        return membership.isEligibleForUpgrade(totalSpending);
    }
}
```

**Purpose:** Creates the right membership object based on input.

---

### 4️⃣ **Factory Usage** (in `CustomerService.java`)

```java
@Service
public class CustomerService {
    
    @Autowired
    private MembershipFactory membershipFactory;
    
    public CustomerDTO getCustomerDetails(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        
        // Use factory to create membership object
        MembershipLevel membershipLevel = membershipFactory
            .createMembershipBySpending(customer.getTotalSpending());
        
        // Get membership benefits
        dto.setMembershipLevel(membershipLevel.getLevelName());
        dto.setDiscountPercentage(membershipLevel.getDiscountPercentage());
        dto.setPointsMultiplier(membershipLevel.getPointsMultiplier());
        dto.setBenefitsDescription(membershipLevel.getBenefitsDescription());
        
        // Check if upgrade available
        if (membershipLevel.isEligibleForUpgrade(customer.getTotalSpending())) {
            dto.setNextLevelName(membershipLevel.getNextLevelName());
            dto.setUpgradeAvailable(true);
        }
        
        return dto;
    }
}
```

---

## 🎨 How to Identify Factory Pattern in UI

### **Where to Look:**

1. **Customer View Page** (`customers/view.html`)
2. **Customer Reports** (`customers/reports.html`)
3. **Bill/Checkout Pages**

---

### 🔍 **Visual Indicators in UI**

#### **1. Customer Profile Page**

When you view a customer, you'll see:

```
┌─────────────────────────────────────┐
│ 👤 Customer Profile                 │
├─────────────────────────────────────┤
│ Name: John Doe                      │
│ Email: john@example.com             │
│                                     │
│ 🏆 Membership & Loyalty             │
│ ├─ Level: [Platinum] 🌟            │  ← Factory creates Platinum object
│ ├─ Discount: 15%                   │  ← From PlatinumMembership.getDiscountPercentage()
│ ├─ Points Multiplier: 3.0x         │  ← From PlatinumMembership.getPointsMultiplier()
│ ├─ Total Spending: $12,450.00      │  ← Input to factory
│ └─ Benefits: Premium privileges... │  ← From PlatinumMembership.getBenefitsDescription()
└─────────────────────────────────────┘
```

**How Factory Works Here:**
```java
// Behind the scenes
double totalSpending = 12450.00;
MembershipLevel membership = membershipFactory.createMembershipBySpending(totalSpending);
// Returns: PlatinumMembership object (since $12,450 >= $10,000)

String level = membership.getLevelName();        // "Platinum"
double discount = membership.getDiscountPercentage();  // 15.0
double points = membership.getPointsMultiplier();      // 3.0
```

---

#### **2. Membership Badge Colors**

The UI shows different colored badges based on factory-created membership:

```html
<span class="badge" 
      th:classappend="${customer.membershipLevel == 'Platinum' ? 'badge-info' : 
                       customer.membershipLevel == 'Gold' ? 'badge-warning' :
                       customer.membershipLevel == 'Silver' ? 'badge-secondary' : 'badge-primary'}"
      th:text="${customer.membershipLevel}">
</span>
```

**Visual Result:**
- 🔵 **Standard** (Blue badge)
- ⚪ **Silver** (Gray badge)
- 🟡 **Gold** (Yellow badge)
- 🔷 **Platinum** (Cyan badge)

---

#### **3. Customer Reports - Membership Distribution**

```
┌─────────────────────────────────────────┐
│ 📊 Membership Level Distribution        │
├─────────────────────────────────────────┤
│ Standard  ████████░░ (25 customers, 40%)│  ← Factory creates Standard
│ Silver    ██████░░░░ (15 customers, 24%)│  ← Factory creates Silver
│ Gold      ████░░░░░░ (12 customers, 19%)│  ← Factory creates Gold
│ Platinum  ███░░░░░░░ (10 customers, 16%)│  ← Factory creates Platinum
└─────────────────────────────────────────┘
```

**How It Works:**
```java
// For each customer in the database
for (Customer customer : allCustomers) {
    MembershipLevel membership = membershipFactory
        .createMembershipBySpending(customer.getTotalSpending());
    
    String level = membership.getLevelName();
    membershipCounts.put(level, membershipCounts.get(level) + 1);
}
```

---

#### **4. Upgrade Notifications**

When viewing a customer who's eligible for upgrade:

```
┌─────────────────────────────────────────┐
│ 🎉 Upgrade Available!                   │
├─────────────────────────────────────────┤
│ Current Level: Silver                   │
│ Total Spending: $5,200                  │
│                                         │
│ You qualify for: Gold ⭐                │
│ Benefits: 10% discount, 2.0x points     │
│                                         │
│ [Upgrade Now]                           │
└─────────────────────────────────────────┘
```

**Behind the Scenes:**
```java
MembershipLevel currentLevel = membershipFactory.createMembership("Silver");
double totalSpending = 5200.00;

if (currentLevel.isEligibleForUpgrade(totalSpending)) {
    MembershipLevel nextLevel = membershipFactory.getNextLevel("Silver");
    // Returns: GoldMembership object
    
    String nextName = nextLevel.getLevelName();          // "Gold"
    double nextDiscount = nextLevel.getDiscountPercentage();  // 10.0
}
```

---

#### **5. Bill/Checkout - Discount Application**

When creating a bill for a customer:

```
┌─────────────────────────────────────────┐
│ 🧾 Bill Summary                         │
├─────────────────────────────────────────┤
│ Customer: John Doe (Platinum Member)    │  ← Factory identifies level
│                                         │
│ Subtotal:        $100.00                │
│ Membership Discount (15%): -$15.00     │  ← From factory-created object
│ Points Earned:   300 pts (3.0x)        │  ← From factory-created object
│                                         │
│ Total:           $85.00                 │
└─────────────────────────────────────────┘
```

**Code:**
```java
// In BillService
MembershipLevel membership = membershipFactory
    .createMembershipBySpending(customer.getTotalSpending());

double subtotal = 100.00;
double discountPercent = membership.getDiscountPercentage();  // 15.0
double discount = subtotal * (discountPercent / 100);         // $15.00
double total = subtotal - discount;                           // $85.00

double basePoints = subtotal;                                 // 100
double pointsMultiplier = membership.getPointsMultiplier();   // 3.0
double earnedPoints = basePoints * pointsMultiplier;          // 300
```

---

## 🎯 How to Test Factory Pattern in Your UI

### **Test Scenario 1: View Different Membership Levels**

1. **Create customers with different spending:**
   - Customer A: $500 total spending → Should show **Standard**
   - Customer B: $2,000 total spending → Should show **Silver**
   - Customer C: $7,000 total spending → Should show **Gold**
   - Customer D: $15,000 total spending → Should show **Platinum**

2. **Check customer profile pages:**
   - Each should show different badge colors
   - Discount percentages should match (0%, 5%, 10%, 15%)
   - Points multipliers should match (1.0x, 1.5x, 2.0x, 3.0x)

---

### **Test Scenario 2: Automatic Upgrade**

1. **Find a Silver member** (total spending $1,000-$4,999)
2. **Make a purchase** that brings total to $5,000+
3. **Refresh customer profile**
4. **Verify:**
   - Badge changed from Silver to Gold
   - Discount changed from 5% to 10%
   - Points multiplier changed from 1.5x to 2.0x

**Factory automatically creates Gold object** when spending threshold is crossed!

---

### **Test Scenario 3: Reports Page**

1. Navigate to **Customers → Reports**
2. Look for **"Membership Level Distribution"** chart
3. Each bar represents customers created by factory
4. Click on different levels to see details

---

## 🔄 Factory Pattern Flow Diagram

```
User Action (UI)
    ↓
Controller receives customer ID
    ↓
Service calls: customerRepository.findById(id)
    ↓
Service gets: customer.getTotalSpending() = $7,500
    ↓
Service calls: membershipFactory.createMembershipBySpending($7,500)
    ↓
Factory Logic:
    if ($7,500 >= $10,000) → Platinum
    if ($7,500 >= $5,000)  → Gold ✓ (Selected)
    if ($7,500 >= $1,000)  → Silver
    else                   → Standard
    ↓
Factory returns: new GoldMembership()
    ↓
Service calls methods:
    - membership.getLevelName() → "Gold"
    - membership.getDiscountPercentage() → 10.0
    - membership.getPointsMultiplier() → 2.0
    - membership.getBenefitsDescription() → "..."
    ↓
Data passed to UI (Thymeleaf)
    ↓
UI displays:
    - Badge: Gold (yellow)
    - Discount: 10%
    - Points: 2.0x
    - Benefits description
```

---

## 🎨 UI Elements That Use Factory Pattern

### **1. Customer List** (`customers/index.html`)
- Shows membership badge for each customer
- Badge color determined by factory-created level

### **2. Customer View** (`customers/view.html`)
- Complete membership details
- Discount percentage
- Points multiplier
- Benefits description
- Upgrade availability

### **3. Customer Reports** (`customers/reports.html`)
- Membership distribution chart
- Count per level
- Percentage breakdown

### **4. Bill Creation** (`bills/create.html`)
- Auto-applies membership discount
- Shows points to be earned
- Customer membership badge

### **5. Customer Edit** (`customers/edit.html`)
- Current membership level (read-only)
- Calculated from total spending

---

## 💡 Benefits You See in UI

### **Without Factory Pattern:**
```java
// MESSY CODE (what you'd have without factory)
if (customer.getTotalSpending() >= 10000) {
    discount = 15.0;
    points = totalSpending * 3.0;
    level = "Platinum";
} else if (customer.getTotalSpending() >= 5000) {
    discount = 10.0;
    points = totalSpending * 2.0;
    level = "Gold";
} // ... repeated everywhere discounts are calculated
```

### **With Factory Pattern:**
```java
// CLEAN CODE (what you have now)
MembershipLevel membership = membershipFactory.createMembershipBySpending(totalSpending);
double discount = membership.getDiscountPercentage();
double points = totalSpending * membership.getPointsMultiplier();
String level = membership.getLevelName();
```

✅ **Consistency** - Same logic everywhere  
✅ **Maintainability** - Change in one place  
✅ **Testability** - Easy to test each membership  
✅ **Extensibility** - Add Diamond level without touching existing code  

---

## 🚀 Adding a New Membership Level (Example)

Want to add a **Diamond** level? Here's how:

### Step 1: Create DiamondMembership.java
```java
public class DiamondMembership implements MembershipLevel {
    private static final double DISCOUNT_PERCENTAGE = 20.0;
    private static final double POINTS_MULTIPLIER = 5.0;
    private static final double MINIMUM_SPENDING = 50000.0;
    
    @Override
    public String getLevelName() { return "Diamond"; }
    // ... implement other methods
}
```

### Step 2: Update Factory
```java
public MembershipLevel createMembershipBySpending(double totalSpending) {
    if (totalSpending >= 50000) return new DiamondMembership();  // NEW
    if (totalSpending >= 10000) return new PlatinumMembership();
    // ... rest unchanged
}
```

### Step 3: Update UI (customers/view.html)
```html
<span class="badge" 
      th:classappend="${customer.membershipLevel == 'Diamond' ? 'badge-diamond' :
                       customer.membershipLevel == 'Platinum' ? 'badge-info' : 
                       ...}">
</span>
```

**That's it!** The factory handles everything else automatically.

---

## 📊 Summary Table

| Membership | Spending Range | Discount | Points | Badge Color |
|------------|---------------|----------|--------|-------------|
| Standard   | $0 - $999     | 0%       | 1.0x   | 🔵 Blue     |
| Silver     | $1,000 - $4,999 | 5%     | 1.5x   | ⚪ Gray     |
| Gold       | $5,000 - $9,999 | 10%    | 2.0x   | 🟡 Yellow   |
| Platinum   | $10,000+      | 15%      | 3.0x   | 🔷 Cyan     |

---

## 🎯 Key Takeaways

1. **Factory Pattern hides complexity** - You don't see `new StandardMembership()` in your code
2. **UI shows factory results** - Badge colors, discounts, points are all from factory objects
3. **Automatic behavior** - Membership upgrades happen automatically based on spending
4. **Centralized logic** - All membership rules in one place (factory + concrete classes)
5. **Easy to extend** - Add new levels without changing existing code

---

## 🔍 Where to Find It in Your Code

**Factory Package:**
- `src/main/java/com/stockmanagement/factory/`
  - `MembershipLevel.java` (Interface)
  - `MembershipFactory.java` (Factory)
  - `impl/StandardMembership.java`
  - `impl/SilverMembership.java`
  - `impl/GoldMembership.java`
  - `impl/PlatinumMembership.java`

**Usage:**
- `CustomerService.java` - Line 210, 229
- `customers/view.html` - Membership display
- `customers/reports.html` - Distribution charts

---

**The Factory Pattern is actively working in your system every time you view a customer, create a bill, or generate reports!** 🏭✨
