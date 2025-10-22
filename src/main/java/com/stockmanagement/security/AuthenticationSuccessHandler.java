package com.stockmanagement.security;

import com.stockmanagement.entity.User;
import com.stockmanagement.entity.UserSession;
import com.stockmanagement.repository.UserSessionRepository;
import com.stockmanagement.service.AuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    public AuthenticationSuccessHandler() {
        // Default remains admin dashboard, but we'll route dynamically per role below
        setDefaultTargetUrl("/admin/dashboard");
        setAlwaysUseDefaultTargetUrl(false);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        HttpSession session = request.getSession();

        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        try {
            // Log successful login
            auditService.logLogin(user, ipAddress, userAgent);

            // Create user session record
            UserSession userSession = new UserSession(
                    session.getId(),
                    user,
                    ipAddress,
                    userAgent,
                    LocalDateTime.now().plusMinutes(30)
            );
            userSessionRepository.save(userSession);

            logger.info("User {} logged in successfully from IP: {}", user.getUsername(), ipAddress);
        } catch (Exception e) {
            // Never break the login flow due to audit/session persistence issues
            logger.error("Post-login processing failed; proceeding with redirect. Cause: {}", e.getMessage(), e);
        }

        // Route by role: ADMIN -> /admin/dashboard, others -> /coming-soon
        String targetUrl = "/coming-soon";
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            targetUrl = "/admin/dashboard";
        }

        try {
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            logger.error("Redirect to {} failed: {}", targetUrl, e.getMessage(), e);
            // Fallback: ensure response is at least redirected to login page to avoid 500
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
