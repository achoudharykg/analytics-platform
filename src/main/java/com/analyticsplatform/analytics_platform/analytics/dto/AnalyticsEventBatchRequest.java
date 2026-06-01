package com.analyticsplatform.analytics_platform.analytics.dto;

import com.analyticsplatform.analytics_platform.analytics.AnalyticsEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AnalyticsEventBatchRequest {

    @NotEmpty(message = "Event list should not be empty")
    @Valid
    private List<AnalyticsEventRequest> events;
}
