package com.analyticsplatform.analytics_platform.analytics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class EventFlushJob {
    private final StringRedisTemplate redisTemplate;
    private final AnalyticsEventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 30000)
    public void flushEvents() {
        Set<String> keys = redisTemplate.keys("events:buffer:*");

        if(keys == null || keys.isEmpty()) return;

        for(String key : keys) {
            List<AnalyticsEvent> batch = new ArrayList<>();

            String json;
            while((json = redisTemplate.opsForList().leftPop(key)) != null) {
                try {
                    AnalyticsEvent event = objectMapper.readValue(json, AnalyticsEvent.class);
                    batch.add(event);
                } catch (Exception e) {
                    log.error("Failed to deserialize event: {}", e.getMessage());
                }

                if(batch.size() >= 500) {
                    eventRepository.saveAll(batch);
                    log.info("Flushed {} events from {}", batch.size(), key);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                eventRepository.saveAll(batch);
                log.info("Flushed {} events from {}", batch.size(), key);
            }
        }
    }
}
