package com.analyticsplatform.analytics_platform.analytics;

import com.analyticsplatform.analytics_platform.analytics.dto.AnalyticsEventBatchRequest;
import com.analyticsplatform.analytics_platform.analytics.dto.AnalyticsEventRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.EventRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void bufferEvents(
            Long projectId,
            String environment,
            AnalyticsEventBatchRequest request
    ) {
       String redisKey = "events:buffer:" + projectId + ":" + environment;

       for(AnalyticsEventRequest analyticsEventRequest: request.getEvents()) {
           AnalyticsEvent entity = new AnalyticsEvent();

           entity.setProjectId(projectId);
           entity.setEnvironment(environment);
           entity.setEventName(analyticsEventRequest.getEventName());
           entity.setEventTimeStamp(analyticsEventRequest.getTimestamp());
           entity.setDeviceId(analyticsEventRequest.getDeviceId());
           entity.setMetaData(convertMetadata(analyticsEventRequest.getMetadata()));

           try {
               String json = objectMapper.writeValueAsString(entity);
               redisTemplate.opsForList().rightPush(redisKey, json);
           } catch (JsonProcessingException e) {
               throw new RuntimeException("Failed to serialize event", e);
           }
       }
    }

    public Page<AnalyticsEvent> getEvents(
            Long projectId,
            String environment,
            String eventName,
            Pageable pageable
    ) {
        if(eventName != null && !eventName.isBlank()) {
            return analyticsEventRepository.findByProjectIdAndEnvironmentAndEventName(
                    projectId,
                    environment,
                    eventName,
                    pageable
            );
        }

        return analyticsEventRepository.findByProjectIdAndEnvironment(projectId, environment, pageable);
    }

    private String convertMetadata(Map<String, Object> metadata) {
        if (metadata == null) return null;
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
