package com.skmj.server.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lc
 */
public class RequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        
        System.out.println("[{}] 收到请求: {} {}".formatted(
            requestTime,
            httpRequest.getMethod(),
            httpRequest.getRequestURI()
        ));
        
        // 记录响应状态码
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        LoggingResponseWrapper responseWrapper = new LoggingResponseWrapper(httpResponse);
        
        chain.doFilter(request, responseWrapper);
        
        System.out.println("[{}] 请求结束: 响应状态码 {}".formatted(
            requestTime,
            responseWrapper.getStatus()
        ));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

// 响应包装类用于获取响应状态码
class LoggingResponseWrapper extends HttpServletResponseWrapper {
    private int status;

    public LoggingResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int sc) throws IOException {
        status = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        status = sc;
        super.sendError(sc, msg);
    }

    @Override
    public void setStatus(int sc) {
        status = sc;
        super.setStatus(sc);
    }
    @Override
    public int getStatus() {
        return status;
    }
}