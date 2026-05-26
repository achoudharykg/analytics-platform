package com.analyticsplatform.analytics_platform.project;

import com.analyticsplatform.analytics_platform.project.dto.CreateProjectRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ApiKeyRepository apiKeyRepository;

    public Project createProject(Long userId, CreateProjectRequest request) {
        Project project = new Project();
        project.setUserId(userId);
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return projectRepository.save(project);
    }

    public List<Project> getProjectByUser(Long userId) {
        return projectRepository.findByUserId(userId);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }

    public String generateApiKey(Long projectId, String environment) {
        getProjectById(projectId);

        String rawKey = "ak_" + environment + "_" +  UUID.randomUUID().toString().replace("-", "");

        ApiKey apiKey = new ApiKey();
        apiKey.setProjectId(projectId);
        apiKey.setEnvironment(environment);
        apiKey.setKeyHash(hashKey(rawKey));
        apiKey.setKeyPrefix(rawKey.substring(0, 8));

        apiKeyRepository.save(apiKey);

        return rawKey;
    }

    public void revokeApiKey(Long keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("API key not found"));
        apiKey.setActive(false);
        apiKey.setRevokedAt(java.time.LocalDateTime.now());
        apiKeyRepository.save(apiKey);
    }

    public String hashKey(String rawKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
