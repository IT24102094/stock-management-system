# Form Theme Quick Reference

## Standard HTML Structure for Form Pages

### CREATE PAGE Template
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create [Entity] - Stock Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link th:href="@{/css/form-theme.css}" rel="stylesheet">
</head>
<body>
    <div class="bg-animation" id="bgAnimation"></div>
    
    <div class="container-fluid">
        <div class="form-container">
            <div class="form-header">
                <div class="user-avatar">
                    <i class="fas fa-[icon-name]"></i>
                </div>
                <h2>Create [Entity Name]</h2>
                <p>Description text here</p>
            </div>

            <form th:action="@{/path}" method="post" th:object="${object}">
                <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
                
                <!-- Form fields go here -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="field" class="form-label">
                                <i class="fas fa-icon me-2"></i>Label <span class="text-danger">*</span>
                            </label>
                            <input type="text" class="form-control" id="field" th:field="*{field}" 
                                   placeholder="Enter text" required>
                            <div class="invalid-feedback" th:if="${#fields.hasErrors('field')}" 
                                 th:errors="*{field}"></div>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="/back-url" class="btn btn-secondary-modern">
                        <i class="fas fa-arrow-left me-2"></i>Cancel
                    </a>
                    <button type="submit" class="btn btn-modern">
                        <i class="fas fa-plus me-2"></i>Create [Entity]
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

### EDIT PAGE Template
```html
<div class="form-header">
    <div class="d-flex align-items-center">
        <div class="user-edit-icon">
            <i class="fas fa-[icon-name]"></i>
        </div>
        <div class="form-header-content">
            <h2>Edit [Entity]</h2>
            <p>Update information</p>
        </div>
    </div>
    <a href="/back-url" class="btn btn-secondary-modern">
        <i class="fas fa-arrow-left me-2"></i>Back
    </a>
</div>

<form th:action="@{/path/{id}(id=${entity.id})}" method="post" th:object="${entity}">
    <!-- Form fields -->
    
    <div class="form-actions">
        <a href="/back-url" class="btn btn-secondary-modern">
            <i class="fas fa-arrow-left me-2"></i>Cancel
        </a>
        <button type="submit" class="btn btn-modern">
            <i class="fas fa-save me-2"></i>Update [Entity]
        </button>
    </div>
</form>
```

### VIEW PAGE Template
```html
<div class="form-header">
    <div class="d-flex align-items-center">
        <div class="user-view-icon">
            <i class="fas fa-[icon-name]"></i>
        </div>
        <div class="form-header-content">
            <h2>[Entity] Details</h2>
            <p>View information</p>
        </div>
    </div>
    <a href="/back-url" class="btn btn-secondary-modern">
        <i class="fas fa-arrow-left me-2"></i>Back
    </a>
</div>

<div class="info-card">
    <div class="row">
        <div class="col-md-4 text-center mb-4">
            <div class="user-avatar mx-auto mb-3">
                [Avatar or Icon]
            </div>
            <h4 style="color: var(--text-light);" th:text="${entity.name}">Name</h4>
            <span class="badge badge-success">Active</span>
        </div>

        <div class="col-md-8">
            <div class="info-row">
                <div class="info-label"><i class="fas fa-icon me-2"></i>Label</div>
                <div class="info-value" th:text="${entity.field}">Value</div>
            </div>
            <!-- More info-rows -->
        </div>
    </div>
</div>

<div class="form-actions">
    <a th:href="@{/edit-url}" class="btn btn-modern">
        <i class="fas fa-edit me-2"></i>Edit
    </a>
    <a th:href="@{/other-action}" class="btn btn-secondary-modern">
        <i class="fas fa-icon me-2"></i>Action
    </a>
</div>
```

## Available CSS Classes

### Containers
- `.form-container` - Main form wrapper with glass effect
- `.form-header` - Header section
- `.form-actions` - Button container at bottom
- `.info-card` - Card for displaying information (view pages)

### Icons & Avatars
- `.user-avatar` - Large circular icon (120x120px) for create pages
- `.user-edit-icon` - Medium circular icon (80x80px) for edit pages
- `.user-view-icon` - Medium circular icon (80x80px) for view pages

### Form Elements
- `.form-group` - Form field wrapper
- `.form-label` - Label styling
- `.form-control` - Input/textarea styling
- `.form-select` - Select dropdown styling
- `.input-group` - For inputs with prefix/suffix
- `.input-group-text` - Input group addon text
- `.form-check` - Checkbox/radio wrapper
- `.form-check-input` - Checkbox/radio input
- `.form-check-label` - Checkbox/radio label

### Buttons
- `.btn-modern` - Primary gradient button
- `.btn-secondary-modern` - Secondary glass button
- `.btn-danger-modern` - Danger gradient button
- `.btn-info-modern` - Info gradient button

### Info Display (View Pages)
- `.info-row` - Row for displaying label-value pairs
- `.info-label` - Label column (40% width, lighter color)
- `.info-value` - Value column (60% width, white color)

### Badges
- `.badge-success` - Green gradient badge
- `.badge-danger` - Red gradient badge
- `.badge-warning` - Orange gradient badge
- `.badge-info` - Blue gradient badge
- `.badge-primary` - Purple gradient badge

### Alerts
- `.alert-danger` - Red glass alert
- `.alert-success` - Green glass alert
- `.alert-info` - Blue glass alert
- `.alert-warning` - Orange glass alert

### Misc
- `.preview-image` - Image preview (use with .show class)
- `.password-strength` - Password strength bar container
- `.password-strength-bar` - Animated strength bar
- `.availability-check` - Icon position for availability checks

## Icon Recommendations by Module

### Users
- Create: `fa-user-plus`
- Edit: `fa-user-edit`
- View: `fa-user`

### Staff
- Create: `fa-user-tie`
- Edit: `fa-user-tie`
- View: `fa-user-tie`

### Customers
- Create: `fa-user-friends`
- Edit: `fa-user-friends`
- View: `fa-user-friends`

### Promotions
- Create: `fa-tags`
- Edit: `fa-tags`
- View: `fa-tags`

### Discounts
- Create: `fa-percent`
- Edit: `fa-percent`
- View: `fa-percent`

### Bills
- Create: `fa-file-invoice-dollar`
- Edit: `fa-file-invoice-dollar`
- View: `fa-file-invoice-dollar`

## Form Field Icon Suggestions

- Name/Username: `fa-user` or `fa-id-card`
- Email: `fa-envelope`
- Phone: `fa-phone`
- Password: `fa-lock`
- Role: `fa-user-tag`
- Department: `fa-building`
- Date: `fa-calendar`
- Money: `fa-dollar-sign`
- Time: `fa-clock`
- Address: `fa-map-marker-alt`
- Photo: `fa-camera`
- File: `fa-file-upload`
- Status: `fa-toggle-on`
- Category: `fa-folder`
- Description: `fa-align-left`
- Quantity: `fa-sort-numeric-up`
- Search: `fa-search`

## Tips

1. **Always include CSRF token** in forms
2. **Use placeholder text** for better UX
3. **Mark required fields** with `<span class="text-danger">*</span>`
4. **Add invalid-feedback divs** for validation errors
5. **Use proper input types** (email, tel, date, number, etc.)
6. **Group related fields** in sections with h5 headings
7. **Use row/col-md-6** for two-column layouts
8. **Add icons to labels** for visual appeal
9. **Use form-actions** for consistent button placement
10. **Include Cancel button** linking back to list page
