package com.analyticsplatform.analytics_platform.analytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {
    Page<AnalyticsEvent> findByProjectIdAndEnvironment(Long projectId, String environment, Pageable pageable);
    Page<AnalyticsEvent> findByProjectIdAndEnvironmentAndEventName(Long projectId, String environment, String eventName, Pageable pageable);
}
