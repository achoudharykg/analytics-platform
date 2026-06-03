package com.analyticsplatform.analytics_platform.sdk;

import com.analyticsplatform.analytics_platform.analytics.AnalyticsEventService;
import com.analyticsplatform.analytics_platform.analytics.dto.AnalyticsEventBatchRequest;
import com.analyticsplatform.analytics_platform.featureflag.FeatureFlag;
import com.analyticsplatform.analytics_platform.featureflag.FeatureFlagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sdk/v1")
@AllArgsConstructor
public class SdkController {

    private final AnalyticsEventService analyticsEventService;
    private final FeatureFlagService featureFlagService;

    @PostMapping("/events")
    public ResponseEntity<String> ingestEvents(
            @Valid @RequestBody AnalyticsEventBatchRequest request,
            HttpServletRequest httpRequest) {
        Long projectId = (Long) httpRequest.getAttribute("projectId");
        String environment = (String) httpRequest.getAttribute("environment");

        analyticsEventService.bufferEvents(projectId, environment, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Events accepted");
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, Boolean>> getConfig(
            @RequestParam String deviceId,
            HttpServletRequest httpRequest) {
        Long projectId = (Long) httpRequest.getAttribute("projectId");
        String environment = (String) httpRequest.getAttribute("environment");

        List<FeatureFlag> flags = featureFlagService
                .getFlagsByProjectAndEvironment(projectId, environment);

        Map<String, Boolean> config = new HashMap<>();
        for (FeatureFlag flag : flags) {
            config.put(flag.getKey(), featureFlagService.isFlagEnabledForDevice(flag, deviceId));
        }

        return ResponseEntity.ok(config);
    }
}
