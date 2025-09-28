package com.stockmanagement.controller;

import com.stockmanagement.entity.ActionType;
import com.stockmanagement.entity.AuditLog;
import com.stockmanagement.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @GetMapping
    public String reportsDashboard(Model model) {
        // Get summary statistics
        Map<String, Object> summaryStats = reportsService.getSummaryStatistics();
        model.addAttribute("summaryStats", summaryStats);
        
        // Get recent activity
        List<AuditLog> recentActivity = reportsService.getRecentActivity(10);
        model.addAttribute("recentActivity", recentActivity);
        
        // Get action type counts
        Map<ActionType, Long> actionCounts = reportsService.getActionTypeCounts();
        model.addAttribute("actionCounts", actionCounts);
        
        return "admin/reports";
    }

    @GetMapping("/audit-logs")
    public String auditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ActionType actionType,
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AuditLog> auditLogs = reportsService.getAuditLogs(actionType, tableName, startDate, endDate, pageable);
        
        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("actionTypes", ActionType.values());
        model.addAttribute("currentActionType", actionType);
        model.addAttribute("currentTableName", tableName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "admin/audit-logs";
    }

    @GetMapping("/user-activity")
    public String userActivity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long userId,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AuditLog> userActivity = reportsService.getUserActivity(userId, pageable);
        
        model.addAttribute("userActivity", userActivity);
        model.addAttribute("selectedUserId", userId);
        
        return "admin/user-activity";
    }

    @GetMapping("/api/summary")
    @ResponseBody
    public Map<String, Object> getSummaryStats() {
        return reportsService.getSummaryStatistics();
    }

    @GetMapping("/api/activity-chart")
    @ResponseBody
    public Map<String, Object> getActivityChartData(
            @RequestParam(defaultValue = "7") int days) {
        return reportsService.getActivityChartData(days);
    }

    @GetMapping("/api/action-distribution")
    @ResponseBody
    public Map<ActionType, Long> getActionDistribution() {
        return reportsService.getActionTypeCounts();
    }
}