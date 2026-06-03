package com.analyticsplatform.analytics_platform.featureflag;

import com.analyticsplatform.analytics_platform.featureflag.dto.CreateFlagRequest;
import com.analyticsplatform.analytics_platform.featureflag.dto.UpdateFlagRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flags")
@AllArgsConstructor
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    @PostMapping
    public ResponseEntity<FeatureFlag> createFlag(
            Long projectId,
            @Valid @RequestBody CreateFlagRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(featureFlagService.createFlag(projectId, request));
    }

    @GetMapping
    public ResponseEntity<List<FeatureFlag>> getFlags(
            Long projectId,
            String environment
    ) {
        return ResponseEntity
                .ok()
                .body(featureFlagService.getFlagsByProjectAndEvironment(projectId, environment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlag> updateFlag(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFlagRequest request) {
        return ResponseEntity.ok(featureFlagService.updateFlag(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlag(@PathVariable Long id) {
        featureFlagService.deleteFlag(id);
        return ResponseEntity.noContent().build();
    }
}
