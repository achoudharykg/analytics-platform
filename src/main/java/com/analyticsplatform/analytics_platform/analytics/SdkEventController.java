package com.analyticsplatform.analytics_platform.analytics;

import com.analyticsplatform.analytics_platform.analytics.dto.AnalyticsEventBatchRequest;
import com.analyticsplatform.analytics_platform.analytics.dto.AnalyticsEventRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sdk/v1")
@AllArgsConstructor
public class SdkEventController {

    private final AnalyticsEventService analyticsEventService;

    @PostMapping("/events")
    public ResponseEntity<String> ingestEvents(
            @Valid @RequestBody AnalyticsEventBatchRequest request,
            HttpServletRequest httpRequest) {
        Long projectId = (Long) httpRequest.getAttribute("projectId");
        String environment = (String) httpRequest.getAttribute("environment");

        analyticsEventService.bufferEvents(projectId, environment, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Events accepted");
    }
}
