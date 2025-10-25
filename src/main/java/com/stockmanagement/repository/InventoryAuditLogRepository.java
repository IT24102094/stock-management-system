package com.stockmanagement.repository;

import com.stockmanagement.entity.InventoryAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryAuditLogRepository extends JpaRepository<InventoryAuditLog, Long> {

    /**
     * Find all audit logs for a specific item
     */
    List<InventoryAuditLog> findByItemIdOrderByTimestampDesc(Long itemId);

    /**
     * Find audit logs by severity
     */
    List<InventoryAuditLog> findBySeverityOrderByTimestampDesc(String severity);

    /**
     * Find recent audit logs (last N records)
     */
    List<InventoryAuditLog> findTop50ByOrderByTimestampDesc();

    /**
     * Find audit logs within date range
     */
    @Query("SELECT a FROM InventoryAuditLog a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    List<InventoryAuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);

    /**
     * Find audit logs by action type
     */
    List<InventoryAuditLog> findByActionTypeOrderByTimestampDesc(String actionType);

    /**
     * Find critical audit logs (out of stock)
     */
    @Query("SELECT a FROM InventoryAuditLog a WHERE a.severity = 'CRITICAL' ORDER BY a.timestamp DESC")
    List<InventoryAuditLog> findCriticalLogs();

    /**
     * Count audit logs by item
     */
    @Query("SELECT COUNT(a) FROM InventoryAuditLog a WHERE a.item.id = :itemId")
    long countByItemId(@Param("itemId") Long itemId);
}
