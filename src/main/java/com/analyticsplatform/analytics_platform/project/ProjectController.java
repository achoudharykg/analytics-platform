package com.analyticsplatform.analytics_platform.project;

import com.analyticsplatform.analytics_platform.project.dto.CreateProjectRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        Project project = projectService.createProject(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(projectService.getProjectByUser(userId));
    }

    @PostMapping("/{id}/api-keys")
    public ResponseEntity<String> generateApiKey(@PathVariable Long id, @RequestParam String environment) {
        String rawKey = projectService.generateApiKey(id, environment);
        return ResponseEntity.status(HttpStatus.CREATED).body(rawKey);
    }

    @DeleteMapping("/{id}/api-keys/{keyId}")
    public ResponseEntity<Void> revokeApiKey(@PathVariable Long id, @PathVariable Long keyId) {
        projectService.revokeApiKey(keyId);
        return ResponseEntity.noContent().build();
    }
}
