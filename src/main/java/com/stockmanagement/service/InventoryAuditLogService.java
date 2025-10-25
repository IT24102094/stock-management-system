package com.stockmanagement.service;

import com.stockmanagement.entity.InventoryAuditLog;
import com.stockmanagement.repository.InventoryAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryAuditLogService {

    @Autowired
    private InventoryAuditLogRepository auditLogRepository;

    /**
     * Get all audit logs
     */
    public List<InventoryAuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    /**
     * Get recent audit logs (last 50)
     */
    public List<InventoryAuditLog> getRecentAuditLogs() {
        return auditLogRepository.findTop50ByOrderByTimestampDesc();
    }

    /**
     * Get audit logs for a specific item
     */
    public List<InventoryAuditLog> getAuditLogsByItem(Long itemId) {
        return auditLogRepository.findByItemIdOrderByTimestampDesc(itemId);
    }

    /**
     * Get audit logs by severity
     */
    public List<InventoryAuditLog> getAuditLogsBySeverity(String severity) {
        return auditLogRepository.findBySeverityOrderByTimestampDesc(severity);
    }

    /**
     * Get critical audit logs (out of stock events)
     */
    public List<InventoryAuditLog> getCriticalAuditLogs() {
        return auditLogRepository.findCriticalLogs();
    }

    /**
     * Get audit logs by action type
     */
    public List<InventoryAuditLog> getAuditLogsByActionType(String actionType) {
        return auditLogRepository.findByActionTypeOrderByTimestampDesc(actionType);
    }

    /**
     * Get audit logs within date range
     */
    public List<InventoryAuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Get audit log by ID
     */
    public Optional<InventoryAuditLog> getAuditLogById(Long id) {
        return auditLogRepository.findById(id);
    }

    /**
     * Count audit logs for an item
     */
    public long countAuditLogsByItem(Long itemId) {
        return auditLogRepository.countByItemId(itemId);
    }

    /**
     * Delete old audit logs (for maintenance)
     */
    public void deleteAuditLogsBefore(LocalDateTime date) {
        List<InventoryAuditLog> logsToDelete = auditLogRepository.findByDateRange(
            LocalDateTime.of(2000, 1, 1, 0, 0), 
            date
        );
        auditLogRepository.deleteAll(logsToDelete);
    }
}
