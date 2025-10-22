-- ============================================================================
-- STOCK MANAGEMENT SYSTEM - INITIAL DATA
-- Version: 1.0
-- Description: Initial seed data for the application
-- ============================================================================

-- ============================================================================
-- USERS DATA
-- ============================================================================
-- Admin user (Password: password123)
INSERT INTO users (username, email, password_hash, first_name, last_name, role, is_active) 
VALUES ('admin', 'it24102094@my.sliit.lk', '$2a$12$IKEQb00u5QpZMx4v5zMweu.3wrq01acxqr/S7JaTDTIoukJGFzk2y', 'Admin', 'User', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    password_hash = VALUES(password_hash);

-- ============================================================================
-- STAFF DATA
-- ============================================================================
INSERT INTO staff (employee_id, first_name, last_name, email, phone, role, department, hire_date, salary, performance_rating, is_active) 
VALUES 
('EMP001', 'John', 'Doe', 'john.doe@example.com', '1234567890', 'Manager', 'Operations', '2023-01-15', 75000.00, 4.5, 1),
('EMP002', 'Jane', 'Smith', 'jane.smith@example.com', '9876543210', 'Supervisor', 'Warehouse', '2023-02-20', 60000.00, 4.2, 1),
('EMP003', 'Michael', 'Johnson', 'michael.johnson@example.com', '5551234567', 'Inventory Specialist', 'Warehouse', '2023-03-10', 45000.00, 3.8, 1),
('EMP004', 'Sarah', 'Williams', 'sarah.williams@example.com', '7778889999', 'Sales Representative', 'Sales', '2023-04-05', 50000.00, 4.0, 1),
('EMP005', 'David', 'Brown', 'david.brown@example.com', '3334445555', 'Accountant', 'Finance', '2023-05-12', 55000.00, 3.9, 1)
ON DUPLICATE KEY UPDATE
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    email = VALUES(email),
    role = VALUES(role),
    department = VALUES(department);

-- ============================================================================
-- CUSTOMERS DATA
-- ============================================================================
INSERT INTO customers (customer_id, first_name, last_name, email, phone, address, city, postal_code, country, loyalty_points, membership_level)
VALUES 
('CUST001', 'Robert', 'Johnson', 'robert.johnson@example.com', '1112223333', '123 Main St', 'Colombo', '10100', 'Sri Lanka', 150, 'Standard'),
('CUST002', 'Mary', 'Anderson', 'mary.anderson@example.com', '4445556666', '456 Oak Ave', 'Kandy', '20000', 'Sri Lanka', 520, 'Gold'),
('CUST003', 'William', 'Taylor', 'william.taylor@example.com', '7778889999', '789 Pine Rd', 'Galle', '80000', 'Sri Lanka', 75, 'Standard'),
('CUST004', 'Patricia', 'Thomas', 'patricia.thomas@example.com', '3334445555', '101 Elm St', 'Jaffna', '40000', 'Sri Lanka', 300, 'Silver'),
('CUST005', 'James', 'Roberts', 'james.roberts@example.com', '6667778888', '234 Maple Ave', 'Colombo', '10200', 'Sri Lanka', 1200, 'Platinum')
ON DUPLICATE KEY UPDATE
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    email = VALUES(email),
    phone = VALUES(phone),
    address = VALUES(address),
    city = VALUES(city),
    postal_code = VALUES(postal_code),
    country = VALUES(country),
    loyalty_points = VALUES(loyalty_points),
    membership_level = VALUES(membership_level);

-- ============================================================================
-- ITEMS DATA (Inventory)
-- ============================================================================
INSERT INTO item (name, price, quantity, sku, category, description) VALUES
('Laptop', 1200.00, 50, 'TECH001', 'Electronics', 'High-performance laptop for business and gaming'),
('Smartphone', 800.00, 100, 'TECH002', 'Electronics', 'Latest model smartphone with 5G capability'),
('Tablet', 500.00, 75, 'TECH003', 'Electronics', '10-inch tablet for work and entertainment'),
('Monitor', 300.00, 60, 'TECH004', 'Electronics', '27-inch 4K monitor'),
('Keyboard', 80.00, 120, 'TECH005', 'Accessories', 'Mechanical keyboard with RGB lighting'),
('Mouse', 30.00, 200, 'TECH006', 'Accessories', 'Wireless ergonomic mouse'),
('Printer', 250.00, 40, 'TECH007', 'Electronics', 'All-in-one printer with scanner'),
('Headphones', 100.00, 150, 'TECH008', 'Accessories', 'Noise-canceling wireless headphones')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    price = VALUES(price),
    quantity = VALUES(quantity),
    category = VALUES(category),
    description = VALUES(description);

-- ============================================================================
-- BILLS DATA
-- ============================================================================
INSERT INTO bills (bill_number, customer_id, bill_date, total_amount, subtotal_amount, tax_amount, amount_paid, status, payment_method)
VALUES 
('BILL001', 1, '2023-07-10 10:30:00', 2000.00, 1800.00, 200.00, 2000.00, 'PAID', 'CREDIT_CARD'),
('BILL002', 2, '2023-07-11 14:45:00', 1500.00, 1350.00, 150.00, 0.00, 'PENDING', NULL),
('BILL003', 3, '2023-07-12 09:15:00', 800.00, 720.00, 80.00, 400.00, 'PARTIAL', 'CASH'),
('BILL004', 4, '2023-07-13 16:20:00', 350.00, 315.00, 35.00, 350.00, 'PAID', 'BANK_TRANSFER'),
('BILL005', 5, '2023-07-14 11:10:00', 600.00, 540.00, 60.00, 0.00, 'CANCELLED', NULL)
ON DUPLICATE KEY UPDATE
    customer_id = VALUES(customer_id),
    bill_date = VALUES(bill_date),
    total_amount = VALUES(total_amount),
    subtotal_amount = VALUES(subtotal_amount),
    tax_amount = VALUES(tax_amount),
    amount_paid = VALUES(amount_paid),
    status = VALUES(status),
    payment_method = VALUES(payment_method);

-- ============================================================================
-- BILL ITEMS DATA
-- ============================================================================
INSERT INTO bill_items (bill_id, product_id, quantity, unit_price, total_price)
VALUES 
(1, 1, 1, 1200.00, 1200.00),
(1, 5, 10, 80.00, 800.00),
(2, 2, 1, 800.00, 800.00),
(2, 6, 20, 30.00, 600.00),
(2, 8, 1, 100.00, 100.00),
(3, 3, 1, 500.00, 500.00),
(3, 6, 10, 30.00, 300.00),
(4, 5, 3, 80.00, 240.00),
(4, 6, 1, 30.00, 30.00),
(4, 8, 1, 100.00, 100.00),
(5, 7, 1, 250.00, 250.00),
(5, 8, 3, 100.00, 300.00),
(5, 6, 5, 30.00, 150.00);

-- ============================================================================
-- PROMOTIONS DATA
-- ============================================================================
INSERT INTO promotions (name, description, start_date, end_date, active) 
VALUES 
('Summer Sale', 'Special discounts on all summer items', CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 30 DAY), 1),
('Back to School', 'Discounts on laptops and tablets for students', DATE_ADD(CURRENT_DATE(), INTERVAL 15 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 45 DAY), 1),
('Year End Clearance', 'Massive discounts on all items to clear inventory', DATE_ADD(CURRENT_DATE(), INTERVAL 60 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 90 DAY), 1),
('Flash Sale', '24-hour discount event on select items', CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 1 DAY), 1),
('Holiday Special', 'Special promotions for the holiday season', DATE_ADD(CURRENT_DATE(), INTERVAL 90 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 120 DAY), 0)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    active = VALUES(active);

-- ============================================================================
-- PROMOTION ITEMS DATA
-- ============================================================================
INSERT INTO promotion_items (promotion_id, item_id)
SELECT p.id, i.id
FROM promotions p, item i
WHERE p.name = 'Summer Sale' AND i.sku IN ('TECH001', 'TECH002', 'TECH003')
ON DUPLICATE KEY UPDATE promotion_id = promotion_id;

INSERT INTO promotion_items (promotion_id, item_id)
SELECT p.id, i.id
FROM promotions p, item i
WHERE p.name = 'Back to School' AND i.sku IN ('TECH001', 'TECH003')
ON DUPLICATE KEY UPDATE promotion_id = promotion_id;

INSERT INTO promotion_items (promotion_id, item_id)
SELECT p.id, i.id
FROM promotions p, item i
WHERE p.name = 'Year End Clearance' AND i.sku IN ('TECH001', 'TECH002', 'TECH003', 'TECH004', 'TECH005', 'TECH006', 'TECH007', 'TECH008')
ON DUPLICATE KEY UPDATE promotion_id = promotion_id;

INSERT INTO promotion_items (promotion_id, item_id)
SELECT p.id, i.id
FROM promotions p, item i
WHERE p.name = 'Flash Sale' AND i.sku IN ('TECH002', 'TECH006', 'TECH008')
ON DUPLICATE KEY UPDATE promotion_id = promotion_id;

INSERT INTO promotion_items (promotion_id, item_id)
SELECT p.id, i.id
FROM promotions p, item i
WHERE p.name = 'Holiday Special' AND i.sku IN ('TECH001', 'TECH002', 'TECH004', 'TECH008')
ON DUPLICATE KEY UPDATE promotion_id = promotion_id;

-- ============================================================================
-- DISCOUNTS DATA
-- ============================================================================
INSERT INTO discounts (name, description, type, value, start_date, end_date, active, item_id)
SELECT 'Laptop Discount', '15% off all laptops', 'PERCENTAGE', 15.00, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 30 DAY), 1, id
FROM item WHERE sku = 'TECH001'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    type = VALUES(type),
    value = VALUES(value),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    active = VALUES(active);

INSERT INTO discounts (name, description, type, value, start_date, end_date, active, item_id)
SELECT 'Smartphone Special', '$100 off all smartphones', 'FIXED_AMOUNT', 100.00, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 14 DAY), 1, id
FROM item WHERE sku = 'TECH002'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    type = VALUES(type),
    value = VALUES(value),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    active = VALUES(active);

INSERT INTO discounts (name, description, type, value, start_date, end_date, active, item_id)
SELECT 'Tablet Flash Sale', '20% off tablets today only', 'PERCENTAGE', 20.00, CURRENT_DATE(), CURRENT_DATE(), 1, id
FROM item WHERE sku = 'TECH003'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    type = VALUES(type),
    value = VALUES(value),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    active = VALUES(active);

INSERT INTO discounts (name, description, type, value, start_date, end_date, active, item_id)
SELECT 'Monitor Sale', '$50 off all monitors', 'FIXED_AMOUNT', 50.00, DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 37 DAY), 1, id
FROM item WHERE sku = 'TECH004'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    type = VALUES(type),
    value = VALUES(value),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    active = VALUES(active);
