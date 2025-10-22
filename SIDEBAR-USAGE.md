# Sidebar Fragment Usage Guide

## Overview
The application now has a **unified sidebar fragment** at:
`src/main/resources/templates/fragments/sidebar.html`

This ensures all pages have the exact same navigation bar with:
- ✅ Consistent styling
- ✅ Same icons (home, user-friends, warehouse, tags, percent, etc.)
- ✅ Automatic active state highlighting
- ✅ All menu items in the same order

## How to Use in Your Pages

### Step 1: Add Thymeleaf namespace
Make sure your HTML has the Thymeleaf namespace:
```html
<html xmlns:th="http://www.thymeleaf.org" lang="en">
```

### Step 2: Replace your sidebar with the fragment

**OLD WAY (Don't use):**
```html
<div class="sidebar">
    <div class="brand">
        <h4><i class="fas fa-boxes"></i> Stock Management</h4>
    </div>
    <nav class="nav flex-column mt-3">
        <a href="/admin/dashboard" class="nav-link">
            <i class="fas fa-home"></i> Dashboard
        </a>
        <!-- ... more links ... -->
    </nav>
</div>
```

**NEW WAY (Use this):**
```html
<div th:replace="~{fragments/sidebar :: sidebar(activeTab='dashboard')}"></div>
```

### Step 3: Set the correct activeTab parameter

Use one of these values for the `activeTab` parameter:
- `'dashboard'` - for Admin Dashboard
- `'users'` - for User Management
- `'suppliers'` - for Supplier Management
- `'staff'` - for Staff Management
- `'customers'` - for Customers
- `'bills'` - for Bills
- `'inventory'` - for Inventory
- `'promotions'` - for Promotions
- `'discounts'` - for Discounts

## Examples

### Dashboard page:
```html
<div th:replace="~{fragments/sidebar :: sidebar(activeTab='dashboard')}"></div>
```

### User Management page:
```html
<div th:replace="~{fragments/sidebar :: sidebar(activeTab='users')}"></div>
```

### Promotions page:
```html
<div th:replace="~{fragments/sidebar :: sidebar(activeTab='promotions')}"></div>
```

### Bills page:
```html
<div th:replace="~{fragments/sidebar :: sidebar(activeTab='bills')}"></div>
```

## Complete Page Example

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - Stock Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        /* Your page styles here */
    </style>
</head>
<body>
    <!-- Use the sidebar fragment -->
    <div th:replace="~{fragments/sidebar :: sidebar(activeTab='users')}"></div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="header">
            <h5>User Management</h5>
            <div class="user-info">
                <span sec:authentication="name">User</span>
                <div class="user-avatar">
                    <i class="fas fa-user"></i>
                </div>
            </div>
        </div>

        <!-- Your page content here -->
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

## Benefits

1. **One Source of Truth**: Change the sidebar once, all pages update
2. **Consistency**: All icons, text, and order are identical
3. **Active State**: Automatically highlights the current page
4. **Easy Maintenance**: Fix bugs or add features in one place
5. **No Duplication**: No need to copy/paste sidebar code

## Pages Currently Using Fragment

The following pages already use fragments:
- ✅ Customers (view.html, edit.html, create.html)
- ✅ Staff (view.html, edit.html, list.html)

## Pages That Need Updating

To use this unified sidebar, these pages need to be updated:
- Admin Dashboard
- User Management
- Supplier Management  
- Bills (list, create, view, print)
- Inventory
- Promotions
- Discounts

## Updating Instructions

For each page:
1. Find the `<div class="sidebar">` section
2. Replace the entire sidebar div with:
   ```html
   <div th:replace="~{fragments/sidebar :: sidebar(activeTab='PAGE_NAME')}"></div>
   ```
3. Replace `'PAGE_NAME'` with the appropriate value from the list above
4. Make sure the CSS styles for `.sidebar`, `.nav-link`, etc. are present
5. Test the page to ensure the sidebar appears and the correct item is highlighted

## Need Help?

If you encounter issues:
1. Check that the Thymeleaf namespace is declared in your `<html>` tag
2. Verify the fragment path is correct: `fragments/sidebar :: sidebar`
3. Ensure the `activeTab` parameter matches one of the valid values
4. Check that your CSS includes styles for `.sidebar`, `.nav-link`, `.nav-link.active`, etc.
