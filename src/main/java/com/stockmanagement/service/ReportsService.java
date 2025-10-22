package com.stockmanagement.service;

import com.stockmanagement.entity.ActionType;
import com.stockmanagement.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

public interface ReportsService {
    Map<String, Object> getSummaryStatistics();
    Map<String, Object> getActivityChartData(int days);
    Map<ActionType, Long> getActionTypeCounts();
    Page<AuditLog> getAuditLogs(ActionType actionType, String tableName, 
                               LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<AuditLog> getUserActivity(Long userId, Pageable pageable);
    java.util.List<AuditLog> getRecentActivity(int limit);
}