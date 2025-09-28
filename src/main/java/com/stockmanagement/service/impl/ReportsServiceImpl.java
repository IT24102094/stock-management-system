package com.stockmanagement.service.impl;

import com.stockmanagement.entity.ActionType;
import com.stockmanagement.entity.AuditLog;
import com.stockmanagement.repository.AuditLogRepository;
import com.stockmanagement.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public Map<String, Object> getSummaryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total audit logs
        long totalLogs = auditLogRepository.count();
        stats.put("totalLogs", totalLogs);
        
        // Logs today
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
        long logsToday = auditLogRepository.findByDateRange(startOfDay, endOfDay, Pageable.unpaged()).getTotalElements();
        stats.put("logsToday", logsToday);
        
        // Logs this week
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay();
        long logsThisWeek = auditLogRepository.findByDateRange(startOfWeek, endOfDay, Pageable.unpaged()).getTotalElements();
        stats.put("logsThisWeek", logsThisWeek);
        
        // Unique users who performed actions
        long uniqueUsers = auditLogRepository.findAll().stream()
                .map(AuditLog::getUser)
                .filter(Objects::nonNull)
                .map(user -> user.getUserId())
                .distinct()
                .count();
        stats.put("uniqueUsers", uniqueUsers);
        
        // Most active table
        String mostActiveTable = auditLogRepository.findAll().stream()
                .map(AuditLog::getTableName)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(table -> table, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        stats.put("mostActiveTable", mostActiveTable);
        
        return stats;
    }

    @Override
    public Map<String, Object> getActivityChartData(int days) {
        Map<String, Object> chartData = new HashMap<>();
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        
        // Get daily activity counts
        List<Map<String, Object>> dailyActivity = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime dayStart = LocalDateTime.now().minusDays(i).toLocalDate().atStartOfDay();
            LocalDateTime dayEnd = LocalDateTime.now().minusDays(i).toLocalDate().atTime(23, 59, 59);
            
            long count = auditLogRepository.findByDateRange(dayStart, dayEnd, Pageable.unpaged()).getTotalElements();
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dayStart.format(DateTimeFormatter.ofPattern("MMM dd")));
            dayData.put("count", count);
            dailyActivity.add(dayData);
        }
        
        chartData.put("dailyActivity", dailyActivity);
        
        // Get action type distribution for the period
        Map<ActionType, Long> actionCounts = auditLogRepository.findByDateRange(startDate, endDate, Pageable.unpaged())
                .getContent().stream()
                .collect(Collectors.groupingBy(AuditLog::getActionType, Collectors.counting()));
        
        chartData.put("actionDistribution", actionCounts);
        
        return chartData;
    }

    @Override
    public Map<ActionType, Long> getActionTypeCounts() {
        return auditLogRepository.findAll().stream()
                .collect(Collectors.groupingBy(AuditLog::getActionType, Collectors.counting()));
    }

    @Override
    public Page<AuditLog> getAuditLogs(ActionType actionType, String tableName, 
                                      LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (actionType != null && tableName != null && startDate != null && endDate != null) {
            // Filter by all parameters - get all logs and filter in memory for now
            // In a real application, you'd want to add custom repository methods
            return auditLogRepository.findByDateRange(startDate, endDate, pageable)
                    .map(log -> {
                        if (log.getActionType() == actionType && 
                            (tableName.equals("") || tableName.equals(log.getTableName()))) {
                            return log;
                        }
                        return null;
                    });
        } else if (actionType != null) {
            return auditLogRepository.findByActionType(actionType, pageable);
        } else if (tableName != null) {
            return auditLogRepository.findByTableName(tableName, pageable);
        } else if (startDate != null && endDate != null) {
            return auditLogRepository.findByDateRange(startDate, endDate, pageable);
        } else {
            return auditLogRepository.findAll(pageable);
        }
    }

    @Override
    public Page<AuditLog> getUserActivity(Long userId, Pageable pageable) {
        if (userId != null) {
            // This would need a custom query in the repository
            // For now, return all logs and filter in service
            return auditLogRepository.findAll(pageable)
                    .map(log -> {
                        if (log.getUser() != null && log.getUser().getUserId().equals(userId)) {
                            return log;
                        }
                        return null;
                    });
        } else {
            return auditLogRepository.findAll(pageable);
        }
    }

    @Override
    public List<AuditLog> getRecentActivity(int limit) {
        return auditLogRepository.findAll(Pageable.ofSize(limit))
                .getContent();
    }
}