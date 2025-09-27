# Login Troubleshooting Guide

## Issue: Cannot login even with correct credentials

### Root Causes Identified:

1. **Database Connection Issues**
2. **No Users in Database** 
3. **Password Encoding Mismatch**

## Solutions:

### Step 1: Verify Database Setup

1. **Ensure MySQL is running** on your system
2. **Create the database** by running the SQL script:
   ```bash
   mysql -u root -p < database_setup.sql
   ```
   Or manually execute the SQL commands in `database_setup.sql`

3. **Verify database connection** in `application.properties`:
   - Database: `stock_management_db`
   - Username: `root` 
   - Password: `Jathu@123`
   - Port: `3306`

### Step 2: Enable Automatic User Creation

The `DataInitializer` has been modified to automatically create a default admin user if none exist.

**Default Credentials:**
- Username: `admin`
- Password: `admin123`

### Step 3: Run the Application

```bash
.\mvnw.cmd spring-boot:run
```

### Step 4: Test Login

1. Open browser and go to: `http://localhost:8080/login`
2. Use credentials: `admin` / `admin123`
3. Check application logs for any errors

### Step 5: Debug Steps

If login still fails:

1. **Check Application Logs** for:
   - Database connection errors
   - User creation messages
   - Authentication failures

2. **Verify User Exists** in database:
   ```sql
   SELECT username, email, role, is_active FROM users;
   ```

3. **Check Password Hash**:
   ```sql
   SELECT username, password_hash FROM users WHERE username = 'admin';
   ```

### Common Issues & Fixes:

#### Issue: "User not found" error
**Solution**: Run the database setup script or check if DataInitializer created the user

#### Issue: "Invalid credentials" error  
**Solution**: Password hash mismatch - ensure using BCrypt with strength 12

#### Issue: "Account disabled" error
**Solution**: Check `is_active` field in users table

#### Issue: Database connection error
**Solution**: 
- Verify MySQL is running
- Check connection parameters in `application.properties`
- Ensure database `stock_management_db` exists

### Manual User Creation (if needed):

If automatic creation fails, manually insert user:

```sql
INSERT INTO users (username, email, password_hash, first_name, last_name, role, is_active) 
VALUES ('admin', 'admin@stockmanagement.com', '$2a$12$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System', 'Administrator', 'ADMIN', TRUE);
```

### Security Notes:

- Change default password after first login
- Remove debug credentials from login page in production
- Ensure database is properly secured

### Files Modified:

1. `src/main/java/com/stockmanagement/config/DataInitializer.java` - Enabled automatic user creation
2. `database_setup.sql` - Created database setup script
3. `src/main/java/com/stockmanagement/util/PasswordHashGenerator.java` - Utility for password hashing

### Next Steps:

1. Run the database setup script
2. Start the application
3. Test login with admin/admin123
4. Change password after successful login


