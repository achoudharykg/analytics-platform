package com.analyticsplatform.analytics_platform.featureflag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    List<FeatureFlag> findByProjectIdAndEnvironment(Long projectId, String environment);
    boolean existsByProjectIdAndKeyAndEnvironment(Long projectId, String key, String environment);
}
