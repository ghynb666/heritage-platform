package com.heritage.service;

import com.heritage.entity.AccessLog;
import com.heritage.mapper.AccessLogMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AccessLogService {
    private final AccessLogMapper accessLogMapper;

    public AccessLogService(AccessLogMapper accessLogMapper) {
        this.accessLogMapper = accessLogMapper;
    }

    public void recordAccess(String targetType, Long targetId, Long userId, HttpServletRequest request) {
        AccessLog accessLog = new AccessLog();
        accessLog.setUserId(userId);
        accessLog.setTargetType(targetType);
        accessLog.setTargetId(targetId);
        accessLog.setPath(request.getRequestURI());
        accessLog.setIp(resolveIp(request));
        accessLog.setUserAgent(request.getHeader("User-Agent"));
        accessLogMapper.insert(accessLog);
    }

    private String resolveIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.trim().isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
