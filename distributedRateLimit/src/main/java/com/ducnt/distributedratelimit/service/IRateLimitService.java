package com.ducnt.distributedratelimit.service;

import com.ducnt.distributedratelimit.dto.request.RateLimitRequest;

public interface IRateLimitService {
    public boolean handelRateLimit(RateLimitRequest request);
}
