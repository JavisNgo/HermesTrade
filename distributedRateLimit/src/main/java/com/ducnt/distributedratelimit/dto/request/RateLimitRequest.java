package com.ducnt.distributedratelimit.dto.request;

import com.ducnt.distributedratelimit.utils.Util;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateLimitRequest {
    @Nullable
    String clientId;
    @Nullable
    String ipAddress;
    @NotBlank
    String serviceName;
    Long requestTime = Util.getEpochTimeStamp();
}
