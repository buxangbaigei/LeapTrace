package com.example.demo.config;

import com.example.demo.entity.UserToken;
import com.example.demo.mapper.TokenMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String uri = request.getRequestURI();
        if (uri.startsWith("/uploads/") || uri.startsWith("/static/")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权\"}");
            return false;
        }

        String token = authHeader.substring(7);
        UserToken userToken = tokenMapper.findByToken(token);

        if (userToken == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token 无效\"}");
            return false;
        }

        if (userToken.getExpireTime() != null && userToken.getExpireTime().isBefore(java.time.LocalDateTime.now())) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token 已过期\"}");
            return false;
        }

        request.setAttribute("userId", userToken.getUserId());
        return true;
    }
}

