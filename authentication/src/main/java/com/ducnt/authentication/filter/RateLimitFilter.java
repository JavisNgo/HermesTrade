package com.ducnt.authentication.filter;

import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.dto.response.ErrorResponse;
import com.ducnt.authentication.exception.DomainCode;
import com.ducnt.authentication.exception.DomainException;
import com.ducnt.authentication.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimitFilter extends OncePerRequestFilter {
    static int maxAttempts = 5;
    static long TTL_SECONDS = 180L;
    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        LoginRequest loginRequest = null;
        if(!request.getRequestURI().endsWith("/authenticate")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            sendErrorResponse(response, DomainCode.ACCOUNT_INCORRECT);
            return;
        }

        if(loginRequest == null) {
            sendErrorResponse(response, DomainCode.ACCOUNT_INCORRECT);
            return;
        }

        String email = loginRequest.getEmail();

        if (email == null || email.isEmpty()) {
            sendErrorResponse(response, DomainCode.EMAIL_INCORRECT);
            return;
        }
        String key = "rate_limit:auth:" + email;
        Long attempts = (Long) redisTemplate.opsForValue().get(key);
        if (attempts == null) {
            attempts = 0L;
        }
        attempts++;
        if (attempts > maxAttempts) {
            sendErrorResponse(response, DomainCode.RATE_LIMIT_EXCEEDED);
            return;
        }
        redisTemplate.opsForValue().set(key, attempts, TTL_SECONDS, TimeUnit.SECONDS);

        request = new CustomHttpServletRequestWrapper(request, objectMapper.writeValueAsBytes(loginRequest));
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, DomainCode domainCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        String jsonResponse = objectMapper.writeValueAsString(ErrorResponse.fromDomainEnumResponse(domainCode));
        response.getWriter().write(jsonResponse);
    }
}
