package com.stockmanagement.controller;

import com.stockmanagement.entity.InventoryAuditLog;
import com.stockmanagement.service.InventoryAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/inventory/audit")
public class InventoryAuditController {

    @Autowired
    private InventoryAuditLogService auditLogService;

    /**
     * Show audit logs page
     */
    @GetMapping
    public String showAuditLogsPage() {
        return "inventory/audit-logs";
    }

    /**
     * Get all recent audit logs (API)
     */
    @GetMapping("/api/recent")
    @ResponseBody
    public ResponseEntity<List<InventoryAuditLog>> getRecentAuditLogs() {
        try {
            List<InventoryAuditLog> logs = auditLogService.getRecentAuditLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all audit logs (API)
     */
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<InventoryAuditLog>> getAllAuditLogs() {
        try {
            List<InventoryAuditLog> logs = auditLogService.getAllAuditLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get audit logs by item (API)
     */
    @GetMapping("/api/item/{itemId}")
    @ResponseBody
    public ResponseEntity<List<InventoryAuditLog>> getAuditLogsByItem(@PathVariable Long itemId) {
        try {
            List<InventoryAuditLog> logs = auditLogService.getAuditLogsByItem(itemId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get audit logs by severity (API)
     */
    @GetMapping("/api/severity/{severity}")
    @ResponseBody
    public ResponseEntity<List<InventoryAuditLog>> getAuditLogsBySeverity(@PathVariable String severity) {
        try {
            List<InventoryAuditLog> logs = auditLogService.getAuditLogsBySeverity(severity);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get critical audit logs (API)
     */
    @GetMapping("/api/critical")
    @ResponseBody
    public ResponseEntity<List<InventoryAuditLog>> getCriticalAuditLogs() {
        try {
            List<InventoryAuditLog> logs = auditLogService.getCriticalAuditLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get audit logs by date range (API)
     */
    @GetMapping("/api/date-range")
    @ResponseBody
    public ResponseEntity<List<InventoryAuditLog>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<InventoryAuditLog> logs = auditLogService.getAuditLogsByDateRange(startDate, endDate);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Count audit logs for an item (API)
     */
    @GetMapping("/api/count/item/{itemId}")
    @ResponseBody
    public ResponseEntity<Long> countAuditLogsByItem(@PathVariable Long itemId) {
        try {
            long count = auditLogService.countAuditLogsByItem(itemId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
