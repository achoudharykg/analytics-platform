package com.analyticsplatform.analytics_platform.featureflag.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateFlagRequest {

    private String description;
    private boolean enabled;
    @Min(0) @Max(100)
    private int rolloutPercentage;
}