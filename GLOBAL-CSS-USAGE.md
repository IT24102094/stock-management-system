# Global CSS Usage Guide

## Overview
A unified `global.css` file has been created at:
`src/main/resources/static/css/global.css`

This file contains all the theme colors, dimensions, fonts, and component styles used across the entire application.

## How to Use

### In Your HTML Pages

Add this line in the `<head>` section **AFTER** Bootstrap:

```html
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" th:href="@{/css/global.css}">
```

### Remove Inline Styles

**OLD WAY (Don't do this):**
```html
<style>
    :root {
        --bg-primary: #1a1d29;
        ...
    }
    .sidebar { ... }
    .header { ... }
    /* hundreds of lines */
</style>
```

**NEW WAY (Do this):**
```html
<link rel="stylesheet" th:href="@{/css/global.css}">
```

## What's Included

### 1. CSS Variables (Theme Colors)
- `--bg-primary`: #1a1d29 (Main background)
- `--bg-secondary`: #2d3142 (Secondary background - sidebar, header)
- `--bg-card`: #252837 (Card background)
- `--cyan`: #00d9ff (Primary accent color)
- `--orange`: #ff9f43 (Active state, highlights)
- `--green`: #10b981 (Success, active status)
- `--red`: #ef4444 (Error, danger, cancelled status)
- `--yellow`: #fbbf24 (Warning, pending status)
- `--text-primary`: #ffffff (Primary text)
- `--text-secondary`: #9ca3af (Secondary text, muted)
- `--border-color`: #3d4252 (Borders, dividers)

### 2. Layout Components
- **Sidebar**: Fixed 240px width, dark theme, scrollable
- **Main Content**: Auto-adjusts for sidebar (margin-left: 240px)
- **Header**: Sticky top, user info on right
- **Content Wrapper**: Consistent padding (1.25rem)

### 3. UI Components
- **Page Header**: Title, description, action buttons
- **Filter Form**: Search and filter controls
- **Table Card**: Responsive tables with dark theme
- **Forms**: Input fields, selects, textareas
- **Buttons**: Primary, secondary, danger, success variants
- **Badges**: Status indicators (pending, paid, active, etc.)
- **Alerts**: Success, danger, warning, info
- **Modals**: Dark themed dialogs
- **Pagination**: Page navigation controls
- **Cards**: Content containers
- **Dropdowns**: Context menus

### 4. Typography
- **Font Family**: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
- **Base Font Size**: 13px
- **Headings**: Properly sized h1-h6
- **Font Weights**: 400 (normal), 500 (medium), 600 (semibold)

### 5. Responsive Design
- Mobile-first approach
- Sidebar collapses on mobile (<768px)
- Tables become scrollable
- Adjusted padding and spacing

## Complete Page Template

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Title - Stock Management System</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
    <!-- Global Styles -->
    <link rel="stylesheet" th:href="@{/css/global.css}">
    
    <!-- Page-specific styles (if needed) -->
    <style>
        /* Only add page-specific styles here */
    </style>
</head>
<body>
    <!-- Sidebar (use fragment) -->
    <div th:replace="~{fragments/sidebar :: sidebar(activeTab='users')}"></div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Header -->
        <div class="header">
            <h5>Page Title</h5>
            <div class="user-info">
                <span sec:authentication="name">User</span>
                <div class="user-avatar">
                    <i class="fas fa-user"></i>
                </div>
            </div>
        </div>

        <!-- Content Wrapper -->
        <div class="content-wrapper">
            <!-- Page Header -->
            <div class="page-header">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h2>Page Title</h2>
                        <p>Page description</p>
                    </div>
                    <div>
                        <a href="/create" class="btn btn-primary">
                            <i class="fas fa-plus-circle"></i> Create New
                        </a>
                    </div>
                </div>
            </div>

            <!-- Your content here -->
            <div class="table-card">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Column 1</th>
                                <th>Column 2</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Table rows -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

## Pages to Update

Replace inline `<style>` blocks with `<link rel="stylesheet" th:href="@{/css/global.css}">` in:

- ✅ `admin/dashboard.html`
- ✅ `users/list.html`
- ✅ `suppliers/*.html`
- ✅ `staff/*.html`
- ✅ `customers/*.html`
- ✅ `bills/*.html`
- ✅ `Inventory/*.html`
- ✅ `promotions/*.html`
- ✅ `discounts/*.html`

## Benefits

1. **Single Source of Truth**: Change colors once, affects all pages
2. **Consistency**: All pages look identical
3. **Smaller Files**: Pages are much smaller without inline CSS
4. **Browser Caching**: CSS is cached, faster page loads
5. **Easy Maintenance**: Update styles in one place
6. **Version Control**: Track CSS changes separately

## Customization

If a page needs unique styles, add them in a `<style>` block AFTER the global.css link:

```html
<link rel="stylesheet" th:href="@{/css/global.css}">
<style>
    /* Page-specific overrides */
    .special-element {
        background: var(--cyan);
    }
</style>
```

## Testing

After adding global.css to a page:
1. Remove all inline `<style>` blocks
2. Test the page appearance
3. Verify all components (buttons, tables, forms) look correct
4. Check responsive behavior on mobile
5. Ensure no styling is broken

## Support

If you encounter styling issues:
1. Verify the global.css link is correct
2. Check browser console for 404 errors
3. Clear browser cache
4. Ensure Bootstrap and Font Awesome are loaded first
5. Check for conflicting inline styles
