package com.a00n.springsecurityjwtexample.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.a00n.springsecurityjwtexample.utils.JsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        response.getWriter().write(
                new JsonBuilder()
                        .put("timestamp", LocalDateTime.now())
                        .put("status", 403)
                        // .put("message", request.getAttribute("msg"))
                        .put("message", "Access denied")
                        .build());
    }

}
