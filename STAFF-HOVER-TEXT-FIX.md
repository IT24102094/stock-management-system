# Staff Table Hover Text Visibility Fix

## Issue Description
When hovering over rows in the staff management table, the text became invisible/hard to read.

## Root Cause
The CSS hover effect for table rows only set a background color but didn't explicitly ensure text remained visible:

**Before (Problematic Code):**
```css
.table td {
    padding: 1rem;
    vertical-align: middle;
    border: none;
    border-bottom: 1px solid var(--border-color);
    font-size: 0.875rem;
}

.table-hover tbody tr:hover {
    background-color: rgba(255, 165, 67, 0.05);
}
```

**Problem:**
- When hovering, the background changed but text color wasn't explicitly maintained
- This could cause text to become invisible or blend with the background
- Missing explicit text color inheritance on hover state

## Solution Applied
Added explicit text color to table cells and forced text color on hover:

**After (Fixed Code):**
```css
.table td {
    padding: 1rem;
    vertical-align: middle;
    border: none;
    border-bottom: 1px solid var(--border-color);
    font-size: 0.875rem;
    color: var(--text-primary);  /* Added explicit text color */
}

.table-hover tbody tr:hover {
    background-color: rgba(255, 165, 67, 0.05);
}

.table-hover tbody tr:hover td {
    color: var(--text-primary) !important;  /* Force text color on hover */
}
```

**Changes:**
1. Added explicit `color: var(--text-primary)` to `.table td` to ensure base text color
2. Added new rule `.table-hover tbody tr:hover td` to force text color on hover with `!important`
3. This ensures text remains visible (white color) even when row is hovered

## Files Modified
- `src/main/resources/templates/staff/list.html` (lines 207-216)

## Testing Steps
To verify the fix:
1. Rebuild the application: `.\mvnw.cmd clean package -DskipTests`
2. Start the application: `java -jar target\stock-management-system-0.0.1-SNAPSHOT.jar`
3. Navigate to: `http://localhost:8080/staff`
4. Hover over any staff row in the table
5. ✅ Text should remain clearly visible (white color) on the light orange hover background

## Color Variables Used
- `--text-primary: #ffffff` (white) - for text
- `--bg-card: #252837` (dark) - for table background
- `rgba(255, 165, 67, 0.05)` - semi-transparent orange for hover background

## Additional Notes
- The `!important` flag ensures text color takes precedence over any conflicting styles
- This fix maintains the dark theme aesthetic while ensuring proper contrast
- The badges (Active/Inactive) already had proper color definitions and weren't affected by this issue
