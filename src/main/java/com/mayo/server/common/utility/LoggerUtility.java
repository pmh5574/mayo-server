package com.mayo.server.common.utility;

import com.mayo.server.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

public class LoggerUtility {
    public static String clientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static void accessLog(Logger logger, HttpServletRequest request) {
        String method = String.format("%-6s", request.getMethod());
        logger.info("[{}] {} (IP={})", method, request.getRequestURI(), LoggerUtility.clientIP(request));
    }

    public static void errorLog(Logger logger, HttpServletRequest request) {
        String method = String.format("%-6s", request.getMethod());
        logger.error("[{}] {} (IP={})", method, request.getRequestURI(), LoggerUtility.clientIP(request));
    }

    public static void errorLog(Logger logger, HttpServletRequest request, CustomException exception) {
        String method = String.format("%-6s", request.getMethod());
        logger.error("[{}] {} (IP={}) - {}", method, request.getRequestURI(), LoggerUtility.clientIP(request),
                exception.toString());
    }

    public static void errorLog(Logger logger, HttpServletRequest request, Exception exception) {
        String method = String.format("%-6s", request.getMethod());
        logger.error("[{}] {} (IP={}) - UNHANDLED EXCEPTION!!!! {}", method, request.getRequestURI(),
                LoggerUtility.clientIP(request), exception.getClass().getName());
    }

    public static void warnLog(Logger logger, HttpServletRequest request, Exception e) {
        String method = String.format("%-6s", request.getMethod());
        logger.warn("[{}] {} (IP={}) - Exception Name = {}", method, request.getRequestURI(),
                LoggerUtility.clientIP(request), e.getClass().getName());
    }
}
