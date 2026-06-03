package com.analyticsplatform.analytics_platform.featureflag.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateFlagRequest {

    @NotBlank(message = "Flag key is required")
    private String key;

    private String description;

    private boolean enabled = false;

    @Min(0) @Max(100)
    private int rolloutPercentage = 100;

    @NotBlank(message = "Environment is required")
    private String environment;
}
