-- ============================================================================
-- DATABASE RESET SCRIPT
-- Use this to completely reset your database with the new clean structure
-- ============================================================================

-- WARNING: This will delete ALL your data!
-- Make sure you have a backup if needed

DROP DATABASE IF EXISTS stock_management_db;
CREATE DATABASE stock_management_db;
USE stock_management_db;

-- Now restart your Spring Boot application
-- It will automatically create all tables with the new structure
