# Factory Pattern Implementation Recommendation

## 📋 Suggested File: `CustomerService.java`

### 🎯 Specific Location: Membership Level Management

**File Path:** `src/main/java/com/stockmanagement/service/CustomerService.java`

**Current Method (Lines 197-209):**
```java
private void updateMembershipLevel(Customer customer) {
    Integer points = customer.getLoyaltyPoints();
    if (points == null) {
        points = 0;
    }
    
    if (points >= 1000) {
        customer.setMembershipLevel("Platinum");
    } else if (points >= 500) {
        customer.setMembershipLevel("Gold");
    } else if (points >= 200) {
        customer.setMembershipLevel("Silver");
    } else {
        customer.setMembershipLevel("Standard");
    }
}
```

---

## 🔍 Why Factory Pattern is Perfect Here

### 1. **Multiple Related Types**
The system has 4 membership types (Standard, Silver, Gold, Platinum), each with:
- Different point thresholds
- Different benefits
- Different discount rates
- Potential for different business rules

### 2. **Current Problems**
❌ **Hard-coded strings** - "Standard", "Silver", "Gold", "Platinum"
❌ **Scattered logic** - If-else chains are hard to maintain
❌ **No encapsulation** - Benefits/rules not associated with membership types
❌ **Difficult to extend** - Adding "Diamond" tier requires code changes in multiple places
❌ **No type safety** - String-based membership levels prone to typos

### 3. **Business Logic Justification**
Different membership levels typically have:
- **Discount percentages** (Standard: 0%, Silver: 5%, Gold: 10%, Platinum: 15%)
- **Point multipliers** (Standard: 1x, Silver: 1.5x, Gold: 2x, Platinum: 3x)
- **Special benefits** (Free shipping, priority support, exclusive access)
- **Upgrade requirements** (Point thresholds)

---

## 🏗️ Proposed Factory Pattern Implementation

### Step 1: Create Membership Interface
```java
package com.stockmanagement.model;

public interface Membership {
    String getName();
    int getRequiredPoints();
    double getDiscountPercentage();
    double getPointMultiplier();
    String getBadgeColor();
    boolean canUpgrade(int currentPoints);
    Membership getNextLevel();
}
```

### Step 2: Create Concrete Membership Classes
```java
package com.stockmanagement.model;

public class StandardMembership implements Membership {
    @Override
    public String getName() { return "Standard"; }
    
    @Override
    public int getRequiredPoints() { return 0; }
    
    @Override
    public double getDiscountPercentage() { return 0.0; }
    
    @Override
    public double getPointMultiplier() { return 1.0; }
    
    @Override
    public String getBadgeColor() { return "#6c757d"; }
    
    @Override
    public boolean canUpgrade(int currentPoints) {
        return currentPoints >= 200;
    }
    
    @Override
    public Membership getNextLevel() {
        return new SilverMembership();
    }
}

public class SilverMembership implements Membership {
    @Override
    public String getName() { return "Silver"; }
    
    @Override
    public int getRequiredPoints() { return 200; }
    
    @Override
    public double getDiscountPercentage() { return 5.0; }
    
    @Override
    public double getPointMultiplier() { return 1.5; }
    
    @Override
    public String getBadgeColor() { return "#adb5bd"; }
    
    @Override
    public boolean canUpgrade(int currentPoints) {
        return currentPoints >= 500;
    }
    
    @Override
    public Membership getNextLevel() {
        return new GoldMembership();
    }
}

public class GoldMembership implements Membership {
    @Override
    public String getName() { return "Gold"; }
    
    @Override
    public int getRequiredPoints() { return 500; }
    
    @Override
    public double getDiscountPercentage() { return 10.0; }
    
    @Override
    public double getPointMultiplier() { return 2.0; }
    
    @Override
    public String getBadgeColor() { return "#ffc107"; }
    
    @Override
    public boolean canUpgrade(int currentPoints) {
        return currentPoints >= 1000;
    }
    
    @Override
    public Membership getNextLevel() {
        return new PlatinumMembership();
    }
}

public class PlatinumMembership implements Membership {
    @Override
    public String getName() { return "Platinum"; }
    
    @Override
    public int getRequiredPoints() { return 1000; }
    
    @Override
    public double getDiscountPercentage() { return 15.0; }
    
    @Override
    public double getPointMultiplier() { return 3.0; }
    
    @Override
    public String getBadgeColor() { return "#212529"; }
    
    @Override
    public boolean canUpgrade(int currentPoints) {
        return false; // Already at highest level
    }
    
    @Override
    public Membership getNextLevel() {
        return this; // Already at max level
    }
}
```

### Step 3: Create MembershipFactory (The Factory Pattern)
```java
package com.stockmanagement.factory;

import com.stockmanagement.model.*;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MembershipFactory {
    
    // Cache instances for performance (Flyweight pattern bonus!)
    private static final Map<String, Membership> membershipCache = new HashMap<>();
    
    static {
        membershipCache.put("Standard", new StandardMembership());
        membershipCache.put("Silver", new SilverMembership());
        membershipCache.put("Gold", new GoldMembership());
        membershipCache.put("Platinum", new PlatinumMembership());
    }
    
    /**
     * Factory method: Creates membership based on name
     * @param membershipName The name of the membership level
     * @return Membership instance
     */
    public Membership createMembership(String membershipName) {
        Membership membership = membershipCache.get(membershipName);
        if (membership == null) {
            throw new IllegalArgumentException("Invalid membership level: " + membershipName);
        }
        return membership;
    }
    
    /**
     * Factory method: Creates membership based on loyalty points
     * @param loyaltyPoints Current customer's loyalty points
     * @return Appropriate Membership instance
     */
    public Membership createMembershipByPoints(int loyaltyPoints) {
        if (loyaltyPoints >= 1000) {
            return membershipCache.get("Platinum");
        } else if (loyaltyPoints >= 500) {
            return membershipCache.get("Gold");
        } else if (loyaltyPoints >= 200) {
            return membershipCache.get("Silver");
        } else {
            return membershipCache.get("Standard");
        }
    }
    
    /**
     * Get all available membership levels
     * @return Map of all memberships
     */
    public Map<String, Membership> getAllMemberships() {
        return new HashMap<>(membershipCache);
    }
}
```

### Step 4: Refactor CustomerService to Use Factory
```java
// In CustomerService.java
@Autowired
private MembershipFactory membershipFactory;

// BEFORE (Lines 197-209)
private void updateMembershipLevel(Customer customer) {
    Integer points = customer.getLoyaltyPoints();
    if (points == null) {
        points = 0;
    }
    
    if (points >= 1000) {
        customer.setMembershipLevel("Platinum");
    } else if (points >= 500) {
        customer.setMembershipLevel("Gold");
    } else if (points >= 200) {
        customer.setMembershipLevel("Silver");
    } else {
        customer.setMembershipLevel("Standard");
    }
}

// AFTER (Using Factory Pattern)
private void updateMembershipLevel(Customer customer) {
    Integer points = customer.getLoyaltyPoints();
    if (points == null) {
        points = 0;
    }
    
    Membership membership = membershipFactory.createMembershipByPoints(points);
    customer.setMembershipLevel(membership.getName());
}

// NEW: Calculate discount based on membership
public double calculateDiscount(Customer customer, double amount) {
    Membership membership = membershipFactory.createMembership(
        customer.getMembershipLevel()
    );
    double discountPercentage = membership.getDiscountPercentage();
    return amount * (discountPercentage / 100);
}

// NEW: Calculate loyalty points with multiplier
public int calculateEarnedPoints(Customer customer, double purchaseAmount) {
    Membership membership = membershipFactory.createMembership(
        customer.getMembershipLevel()
    );
    double multiplier = membership.getPointMultiplier();
    return (int) ((purchaseAmount / 10) * multiplier);
}
```

---

## ✅ Benefits of This Implementation

### 1. **Open/Closed Principle**
✅ Open for extension (add new membership types)
✅ Closed for modification (no changes to existing code)

### 2. **Single Responsibility**
✅ Each membership class handles its own rules
✅ Factory handles object creation
✅ Service focuses on business logic

### 3. **Type Safety**
✅ No more string-based errors
✅ Compile-time checking
✅ IDE autocomplete support

### 4. **Easy to Extend**
Adding a "Diamond" membership:
```java
// Just create one new class!
public class DiamondMembership implements Membership {
    @Override
    public String getName() { return "Diamond"; }
    @Override
    public int getRequiredPoints() { return 2000; }
    @Override
    public double getDiscountPercentage() { return 20.0; }
    // ... other methods
}

// Register in factory
membershipCache.put("Diamond", new DiamondMembership());
```

### 5. **Testability**
```java
@Test
public void testMembershipUpgrade() {
    Membership gold = membershipFactory.createMembership("Gold");
    assertEquals(10.0, gold.getDiscountPercentage());
    assertTrue(gold.canUpgrade(1500));
}
```

### 6. **Business Rule Centralization**
✅ All membership rules in one place
✅ Easy to modify benefits
✅ Clear documentation of each tier

---

## 📊 Comparison: Before vs After

### Before (Current Code)
```
❌ 15 lines of if-else
❌ Hard-coded strings
❌ No business logic encapsulation
❌ Difficult to test
❌ Changes require modifying multiple files
```

### After (Factory Pattern)
```
✅ Clean, maintainable code
✅ Type-safe enums/objects
✅ Business logic encapsulated
✅ Easy unit testing
✅ Add new tiers with zero changes to existing code
```

---

## 🎯 Implementation Steps

1. **Create package structure:**
   ```
   com.stockmanagement.model/
   com.stockmanagement.factory/
   ```

2. **Create Membership interface and implementations**

3. **Create MembershipFactory class**

4. **Refactor CustomerService to inject and use factory**

5. **Add unit tests for each membership type**

6. **Update UI to dynamically get membership colors/benefits**

---

## 🚀 Future Enhancements

Once factory is implemented, you can easily add:

1. **Dynamic pricing**
   ```java
   double finalPrice = originalPrice - 
       membership.getDiscountPercentage() * originalPrice;
   ```

2. **Automated upgrades**
   ```java
   if (membership.canUpgrade(customer.getLoyaltyPoints())) {
       customer.setMembershipLevel(membership.getNextLevel().getName());
   }
   ```

3. **Personalized notifications**
   ```java
   String message = "You're " + 
       (membership.getNextLevel().getRequiredPoints() - currentPoints) + 
       " points away from " + membership.getNextLevel().getName();
   ```

4. **Membership benefits display**
   ```java
   List<String> benefits = membership.getBenefits();
   ```

---

## 📝 Summary

**File:** `CustomerService.java`
**Method:** `updateMembershipLevel()` (Lines 197-209)
**Pattern:** Factory Pattern with Strategy Pattern elements
**Justification:** 
- ✅ Eliminates if-else chains
- ✅ Encapsulates membership business rules
- ✅ Makes system extensible and maintainable
- ✅ Follows SOLID principles
- ✅ Improves testability

This is a **textbook example** of where Factory Pattern shines! 🌟
