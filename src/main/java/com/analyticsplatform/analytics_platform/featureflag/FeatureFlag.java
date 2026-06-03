package com.analyticsplatform.analytics_platform.featureflag;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "feature_flags",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "key", "environment"}))
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private String key;

    private String description;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    private int rolloutPercentage = 100;

    @Column(nullable = false)
    private String environment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
