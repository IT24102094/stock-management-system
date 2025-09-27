package com.stockmanagement.repository;

import com.stockmanagement.entity.ActionType;
import com.stockmanagement.entity.AuditLog;
import com.stockmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByUser(User user, Pageable pageable);
    Page<AuditLog> findByActionType(ActionType actionType, Pageable pageable);
    Page<AuditLog> findByTableName(String tableName, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    Page<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.user = :user AND a.actionType = :actionType ORDER BY a.createdAt DESC")
    List<AuditLog> findByUserAndActionType(@Param("user") User user, @Param("actionType") ActionType actionType);
}
