package com.analyticsplatform.analytics_platform.analytics;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AnalyticsController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/events")
    public ResponseEntity<Page<AnalyticsEvent>> getEvents(
            @RequestParam Long projectId,
            @RequestParam(defaultValue = "prod") String environment,
            @RequestParam(required = false) String eventName,
            Pageable pageable
    ) {
        return ResponseEntity.ok(analyticsEventService.getEvents(projectId, environment, eventName, pageable));
    }
}
