package com.analyticsplatform.analytics_platform.analytics;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "analytics_events")
public class AnalyticsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String environment;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String metaData;

    @Column(nullable = false)
    private LocalDateTime eventTimeStamp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime ingestedAt;

    @PrePersist
    protected void onCreate() {
        this.ingestedAt = LocalDateTime.now();
    }
}
