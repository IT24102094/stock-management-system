# Role-Based Access Control (RBAC) Configuration

## Overview
This document describes the role-based access control implementation for the Stock Management System.

## User Roles and Access Permissions

### 1. ADMIN (Full Access)
**Access:** Complete system access
- ✅ User Management (`/users/**`)
- ✅ Admin Dashboard (`/admin/**`)
- ✅ Supplier Management (`/suppliers/**`)
- ✅ Inventory Management (`/items/**`)
- ✅ Customer Management (`/customers/**`)
- ✅ Bills Management (`/bills/**`)
- ✅ Staff Management (`/staff/**`)
- ✅ Promotions (`/promotions/**`)
- ✅ Discounts (`/discounts/**`)
- **Login Redirect:** `/admin/dashboard`

### 2. STOCK_MANAGER
**Access:** Suppliers and Inventory
- ✅ Supplier Management (`/suppliers/**`)
- ✅ Inventory Management (`/items/**`)
- ❌ User Management
- ❌ Customer Management
- ❌ Bills Management
- ❌ Staff Management
- ❌ Promotions
- ❌ Discounts
- **Login Redirect:** `/items/dashboard`

### 3. SALES_STAFF
**Access:** Customers and Bills
- ✅ Customer Management (`/customers/**`)
- ✅ Bills Management (`/bills/**`)
- ❌ User Management
- ❌ Supplier Management
- ❌ Inventory Management
- ❌ Staff Management
- ❌ Promotions
- ❌ Discounts
- **Login Redirect:** `/bills`

### 4. HR_STAFF
**Access:** Staff Management
- ✅ Staff Management (`/staff/**`)
- ❌ User Management
- ❌ Supplier Management
- ❌ Inventory Management
- ❌ Customer Management
- ❌ Bills Management
- ❌ Promotions
- ❌ Discounts
- **Login Redirect:** `/staff`

### 5. MARKETING_MANAGER (New Role)
**Access:** Discounts and Promotions
- ✅ Promotions (`/promotions/**`)
- ✅ Discounts (`/discounts/**`)
- ❌ User Management
- ❌ Supplier Management
- ❌ Inventory Management
- ❌ Customer Management
- ❌ Bills Management
- ❌ Staff Management
- **Login Redirect:** `/promotions`

## Technical Implementation

### Files Modified

1. **UserRole.java**
   - Added `MARKETING_MANAGER` enum value
   - Location: `src/main/java/com/stockmanagement/entity/UserRole.java`

2. **SecurityConfig.java**
   - Updated `filterChain()` method with role-based access rules
   - Configured CSRF exclusions for all protected endpoints
   - Location: `src/main/java/com/stockmanagement/config/SecurityConfig.java`

3. **AuthenticationSuccessHandler.java**
   - Added role-based redirect logic
   - Each role redirects to their primary dashboard after login
   - Location: `src/main/java/com/stockmanagement/security/AuthenticationSuccessHandler.java`

### Security Rules

```java
// ADMIN - Full access
.requestMatchers("/admin/**", "/users/**").hasRole("ADMIN")

// STOCK_MANAGER - Suppliers and Inventory
.requestMatchers("/items/**", "/suppliers/**").hasAnyRole("ADMIN", "STOCK_MANAGER")

// SALES_STAFF - Customers and Bills
.requestMatchers("/customers/**", "/bills/**").hasAnyRole("ADMIN", "SALES_STAFF")

// HR_STAFF - Staff Management
.requestMatchers("/staff/**").hasAnyRole("ADMIN", "HR_STAFF")

// MARKETING_MANAGER - Discounts and Promotions
.requestMatchers("/promotions/**", "/discounts/**").hasAnyRole("ADMIN", "MARKETING_MANAGER")
```

## Creating Users with New Role

When creating a new user through the UI (`/users/create`), the role dropdown will automatically include:
- ADMIN
- STOCK_MANAGER
- SALES_STAFF
- HR_STAFF
- **MARKETING_MANAGER** (NEW)

## Access Denied Behavior

If a user attempts to access a URL they don't have permission for:
- They will receive a **403 Forbidden** error
- Or be redirected to their appropriate dashboard
- The system logs unauthorized access attempts

## Testing RBAC

### Test Cases

1. **ADMIN User:**
   - Should access all URLs successfully
   - Login redirects to `/admin/dashboard`

2. **STOCK_MANAGER User:**
   - Can access `/items/**` and `/suppliers/**`
   - Cannot access `/users/**`, `/bills/**`, etc.
   - Login redirects to `/items/dashboard`

3. **SALES_STAFF User:**
   - Can access `/customers/**` and `/bills/**`
   - Cannot access `/items/**`, `/staff/**`, etc.
   - Login redirects to `/bills`

4. **HR_STAFF User:**
   - Can access `/staff/**`
   - Cannot access other protected resources
   - Login redirects to `/staff`

5. **MARKETING_MANAGER User:**
   - Can access `/promotions/**` and `/discounts/**`
   - Cannot access other protected resources
   - Login redirects to `/promotions`

## Security Notes

- All roles require authentication (`anyRequest().authenticated()`)
- Public access is granted only to: `/`, `/login`, `/css/**`, `/js/**`, `/images/**`, `/favicon.ico`
- Password reset endpoints (`/password/**`) are publicly accessible
- CSRF protection is enabled for all state-changing operations
- Remember-me functionality is available for all roles

## Database Considerations

- Existing users in the database will retain their current roles
- The `MARKETING_MANAGER` role can be assigned to new or existing users
- Update existing user roles via the User Management interface (Admin only)
- Role changes take effect immediately after database update

## Compilation Status

✅ **BUILD SUCCESS** - All changes compiled successfully
- No compilation errors
- Role-based access control is active
- All 5 roles are operational

## Next Steps

1. ✅ Remove DataInitializer (completed)
2. ✅ Add MARKETING_MANAGER role (completed)
3. ✅ Configure role-based access (completed)
4. Test with actual user accounts for each role
5. Consider adding role-based UI element visibility (hide menu items users can't access)
