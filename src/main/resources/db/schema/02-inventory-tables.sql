-- ============================================================================
-- STOCK MANAGEMENT SYSTEM - INVENTORY SCHEMA
-- Version: 2.0
-- Description: Tables for items (inventory) and suppliers
-- ============================================================================

-- Note: Foreign key constraints from other tables will be dropped first in their respective schema files

-- Items Table (Primary Inventory Table)
CREATE TABLE IF NOT EXISTS item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100),
    sku VARCHAR(50) UNIQUE,
    description TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- Add columns if table exists but missing columns (for migration)
SET @dbname = DATABASE();
SET @tablename = 'item';

-- Add sku column if doesn't exist
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = 'sku') = 0,
  'ALTER TABLE item ADD COLUMN sku VARCHAR(50) UNIQUE AFTER category',
  'SELECT "sku column already exists" as message'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Add description column if doesn't exist
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = 'description') = 0,
  'ALTER TABLE item ADD COLUMN description TEXT AFTER sku',
  'SELECT "description column already exists" as message'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Add created_date column if doesn't exist
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = 'created_date') = 0,
  'ALTER TABLE item ADD COLUMN created_date DATETIME DEFAULT CURRENT_TIMESTAMP AFTER description',
  'SELECT "created_date column already exists" as message'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Add updated_date column if doesn't exist
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = 'updated_date') = 0,
  'ALTER TABLE item ADD COLUMN updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_date',
  'SELECT "updated_date column already exists" as message'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Modify id column to BIGINT if it's INT
SET @preparedStatement = (SELECT IF(
  (SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS
   WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = 'id') = 'int',
  'ALTER TABLE item MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT',
  'SELECT "id is already BIGINT" as message'
));
PREPARE alterIfExists FROM @preparedStatement;
EXECUTE alterIfExists;
DEALLOCATE PREPARE alterIfExists;

-- Modify price column to DECIMAL if it's DOUBLE
SET @preparedStatement = (SELECT IF(
  (SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS
   WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = 'price') = 'double',
  'ALTER TABLE item MODIFY COLUMN price DECIMAL(10,2) NOT NULL',
  'SELECT "price is already DECIMAL" as message'
));
PREPARE alterIfExists FROM @preparedStatement;
EXECUTE alterIfExists;
DEALLOCATE PREPARE alterIfExists;

-- Suppliers Table
CREATE TABLE IF NOT EXISTS supplier (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_no VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    rating INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- Indexes for Inventory Tables
CREATE INDEX idx_item_sku ON item(sku);
CREATE INDEX idx_item_category ON item(category);
CREATE INDEX idx_item_name ON item(name);
CREATE INDEX idx_supplier_name ON supplier(name);
CREATE INDEX idx_supplier_email ON supplier(email);
