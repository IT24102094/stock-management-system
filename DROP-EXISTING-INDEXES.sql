-- ============================================================================
-- DROP EXISTING INDEXES SCRIPT
-- Use this if you don't want to reset the entire database
-- ============================================================================

USE stock_management_db;

-- Drop indexes from users table
DROP INDEX IF EXISTS idx_users_username ON users;
DROP INDEX IF EXISTS idx_users_email ON users;
DROP INDEX IF EXISTS idx_users_role ON users;
DROP INDEX IF EXISTS idx_users_active ON users;

-- Drop indexes from audit_log table
DROP INDEX IF EXISTS idx_audit_log_user ON audit_log;
DROP INDEX IF EXISTS idx_audit_log_table ON audit_log;
DROP INDEX IF EXISTS idx_audit_log_created ON audit_log;

-- Drop indexes from item table
DROP INDEX IF EXISTS idx_item_sku ON item;
DROP INDEX IF EXISTS idx_item_category ON item;
DROP INDEX IF EXISTS idx_item_name ON item;

-- Drop indexes from supplier table
DROP INDEX IF EXISTS idx_supplier_name ON supplier;
DROP INDEX IF EXISTS idx_supplier_email ON supplier;

-- Drop indexes from staff table
DROP INDEX IF EXISTS idx_staff_employee_id ON staff;
DROP INDEX IF EXISTS idx_staff_email ON staff;
DROP INDEX IF EXISTS idx_staff_department ON staff;
DROP INDEX IF EXISTS idx_staff_active ON staff;

-- Drop indexes from customers table
DROP INDEX IF EXISTS idx_customers_customer_id ON customers;
DROP INDEX IF EXISTS idx_customers_email ON customers;
DROP INDEX IF EXISTS idx_customers_phone ON customers;
DROP INDEX IF EXISTS idx_customers_membership ON customers;

-- Drop indexes from bills table
DROP INDEX IF EXISTS idx_bills_customer_id ON bills;
DROP INDEX IF EXISTS idx_bills_bill_date ON bills;
DROP INDEX IF EXISTS idx_bills_status ON bills;
DROP INDEX IF EXISTS idx_bills_bill_number ON bills;

-- Drop indexes from bill_items table
DROP INDEX IF EXISTS idx_bill_items_bill_id ON bill_items;
DROP INDEX IF EXISTS idx_bill_items_product_id ON bill_items;

-- Drop indexes from discounts table
DROP INDEX IF EXISTS idx_discounts_active_dates ON discounts;
DROP INDEX IF EXISTS idx_discounts_item ON discounts;

-- Drop indexes from promotions table
DROP INDEX IF EXISTS idx_promotions_active_dates ON promotions;

-- Drop indexes from promotion_items table
DROP INDEX IF EXISTS idx_promotion_items_promotion ON promotion_items;
DROP INDEX IF EXISTS idx_promotion_items_item ON promotion_items;

SELECT 'All indexes dropped successfully!' as Status;
