package com.stockmanagement.service.impl;

import com.stockmanagement.entity.ActionType;
import com.stockmanagement.entity.AuditLog;
import com.stockmanagement.repository.AuditLogRepository;
import com.stockmanagement.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        
        try {
            // Total audit logs
            long totalLogs = auditLogRepository.count();
            stats.put("totalLogs", totalLogs);
            
            // Today's activity
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
            long logsToday = auditLogRepository.findByDateRange(startOfDay, endOfDay, Pageable.unpaged()).getTotalElements();
            stats.put("logsToday", logsToday);
            
            // This week's activity
            LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
            long logsThisWeek = auditLogRepository.findByDateRange(startOfWeek, LocalDateTime.now(), Pageable.unpaged()).getTotalElements();
            stats.put("logsThisWeek", logsThisWeek);
            
            // Unique users count (approximate)
            List<AuditLog> allLogs = auditLogRepository.findAll();
            long uniqueUsers = allLogs.stream()
                    .filter(log -> log.getUser() != null)
                    .map(log -> log.getUser().getUserId())
                    .distinct()
                    .count();
            stats.put("uniqueUsers", uniqueUsers);
            
            // Most active table
            String mostActiveTable = allLogs.stream()
                    .filter(log -> log.getTableName() != null)
                    .collect(Collectors.groupingBy(AuditLog::getTableName, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
            stats.put("mostActiveTable", mostActiveTable);
            
        } catch (Exception e) {
            // If there's any error, return default values
            stats.put("totalLogs", 0);
            stats.put("logsToday", 0);
            stats.put("logsThisWeek", 0);
            stats.put("uniqueUsers", 0);
            stats.put("mostActiveTable", "N/A");
        }
        
        return stats;
    }

    @Override
    public Map<String, Object> getActivityChartData(int days) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            List<Map<String, Object>> dailyActivity = new ArrayList<>();
            
            for (int i = days - 1; i >= 0; i--) {
                LocalDateTime dayStart = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime dayEnd = LocalDateTime.now().minusDays(i).withHour(23).withMinute(59).withSecond(59);
                
                long dayCount = auditLogRepository.findByDateRange(dayStart, dayEnd, Pageable.unpaged()).getTotalElements();
                
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", dayStart.format(DateTimeFormatter.ofPattern("MMM dd")));
                dayData.put("count", dayCount);
                dailyActivity.add(dayData);
            }
            
            chartData.put("dailyActivity", dailyActivity);
            chartData.put("actionDistribution", getActionTypeCounts());
            
        } catch (Exception e) {
            chartData.put("dailyActivity", new ArrayList<>());
            chartData.put("actionDistribution", new HashMap<ActionType, Long>());
        }
        
        return chartData;
    }

    @Override
    public Map<ActionType, Long> getActionTypeCounts() {
        try {
            return auditLogRepository.findAll().stream()
                    .collect(Collectors.groupingBy(AuditLog::getActionType, Collectors.counting()));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Override
    public Page<AuditLog> getAuditLogs(ActionType actionType, String tableName, 
                                      LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        try {
            // If no filters are applied, return all logs
            if (actionType == null && tableName == null && startDate == null && endDate == null) {
                return auditLogRepository.findAll(pageable);
            }
            
            // For now, return all logs - you can enhance this with custom queries later
            return auditLogRepository.findAll(pageable);
        } catch (Exception e) {
            // Return empty page if there's any error
            return Page.empty(pageable);
        }
    }

    @Override
    public Page<AuditLog> getUserActivity(Long userId, Pageable pageable) {
        try {
            if (userId != null) {
                // Find user activities - for now return all, can be enhanced with user filtering
                return auditLogRepository.findAll(pageable);
            }
            return auditLogRepository.findAll(pageable);
        } catch (Exception e) {
            return Page.empty(pageable);
        }
    }

    @Override
    public List<AuditLog> getRecentActivity(int limit) {
        try {
            return auditLogRepository.findAll(PageRequest.of(0, limit, Sort.by("createdAt").descending()))
                    .getContent();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}