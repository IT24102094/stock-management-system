package com.stockmanagement.config;

import com.stockmanagement.entity.User;
import com.stockmanagement.entity.UserRole;
import com.stockmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("DataInitializer starting...");
        
        // Check what users exist
        long userCount = userRepository.count();
        logger.info("Current user count in database: {}", userCount);
        
        if (userCount > 0) {
            logger.info("Users found in database:");
            userRepository.findAll().forEach(user -> {
                logger.info("- User: {} ({}), Role: {}, Active: {}", 
                    user.getUsername(), user.getEmail(), user.getRole(), user.isActive());
            });
        }

        // Always ensure there is a working admin account for access
        ensureAdminUser();
        
        // Add sample users with different roles
        createSampleUsers();
    }

    private void ensureAdminUser() {
        final String username = "admin";
        final String defaultPassword = "admin123";

        userRepository.findByUsername(username).ifPresentOrElse(existing -> {
            boolean needsSave = false;

            if (!existing.isActive()) {
                existing.setActive(true);
                needsSave = true;
            }
            if (existing.getRole() == null || existing.getRole() != UserRole.ADMIN) {
                existing.setRole(UserRole.ADMIN);
                needsSave = true;
            }

            // Reset password to a known value to avoid hash mismatch during troubleshooting
            String encoded = passwordEncoder.encode(defaultPassword);
            if (!passwordEncoder.matches(defaultPassword, existing.getPasswordHash())) {
                existing.setPasswordHash(encoded);
                needsSave = true;
            }

            if (needsSave) {
                userRepository.save(existing);
                logger.warn("Existing admin user updated (active/role/password reset for access).");
            } else {
                logger.info("Admin user already present and active.");
            }
            logger.info("Login credentials - Username: admin, Password: {}", defaultPassword);
        }, () -> {
            logger.warn("Admin user not found. Creating default admin user...");
            User adminUser = new User();
            adminUser.setUsername(username);
            adminUser.setEmail("admin@stockmanagement.com");
            adminUser.setPasswordHash(passwordEncoder.encode(defaultPassword));
            adminUser.setFirstName("System");
            adminUser.setLastName("Administrator");
            adminUser.setRole(UserRole.ADMIN);
            adminUser.setActive(true);
            userRepository.save(adminUser);
            logger.info("Default admin user created. Login with admin / {}", defaultPassword);
        });
    }
    
    private void createSampleUsers() {
        logger.info("Creating sample users with different roles...");
        
        // Get admin user for createdBy reference
        User adminUser = userRepository.findByUsername("admin")
            .orElseThrow(() -> new RuntimeException("Admin user not found"));
        
        String defaultPassword = "password123";
        String encodedPassword = passwordEncoder.encode(defaultPassword);
        
        // Sample users with different roles
        createUserIfNotExists("stockmgr", "stockmgr@example.com", encodedPassword, 
            "Stock", "Manager", UserRole.STOCK_MANAGER, adminUser);
        
        createUserIfNotExists("sales1", "sales1@example.com", encodedPassword, 
            "Sales", "Staff", UserRole.SALES_STAFF, adminUser);
        
        createUserIfNotExists("hr1", "hr1@example.com", encodedPassword, 
            "HR", "Staff", UserRole.HR_STAFF, adminUser);
        
        createUserIfNotExists("demo_inactive", "demo.inactive@example.com", encodedPassword, 
            "Demo", "User", UserRole.SALES_STAFF, adminUser, false);
        
        logger.info("Sample users created. All users can login with password: {}", defaultPassword);
    }
    
    private void createUserIfNotExists(String username, String email, String passwordHash, 
                                     String firstName, String lastName, UserRole role, User createdBy) {
        createUserIfNotExists(username, email, passwordHash, firstName, lastName, role, createdBy, true);
    }
    
    private void createUserIfNotExists(String username, String email, String passwordHash, 
                                     String firstName, String lastName, UserRole role, User createdBy, boolean isActive) {
        userRepository.findByUsername(username).ifPresentOrElse(existing -> {
            logger.info("User {} already exists, skipping creation", username);
        }, () -> {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(passwordHash);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(role);
            user.setActive(isActive);
            user.setCreatedBy(createdBy);
            user.setUpdatedBy(createdBy);
            
            userRepository.save(user);
            logger.info("Created user: {} ({}) with role: {}", username, email, role);
        });
    }
}
