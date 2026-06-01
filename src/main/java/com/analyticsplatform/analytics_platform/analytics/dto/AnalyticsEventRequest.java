package com.analyticsplatform.analytics_platform.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class AnalyticsEventRequest {

    @NotBlank
    private String eventName;

    @NotBlank
    private String deviceId;

    @NotNull
    private LocalDateTime timestamp;

    private Map<String, Object> metadata;
}
