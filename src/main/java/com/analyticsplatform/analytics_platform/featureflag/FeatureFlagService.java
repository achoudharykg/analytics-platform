package com.analyticsplatform.analytics_platform.featureflag;

import com.analyticsplatform.analytics_platform.featureflag.dto.CreateFlagRequest;
import com.analyticsplatform.analytics_platform.featureflag.dto.UpdateFlagRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository flagRepository;

    public FeatureFlag createFlag(Long projectId, CreateFlagRequest request) {
        if(flagRepository.existsByProjectIdAndKeyAndEnvironment(projectId, request.getKey(), request.getEnvironment())) {
            throw new RuntimeException("Flag '" + request.getKey() + "' already exists in " + request.getEnvironment());
        }

        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setEnvironment(request.getEnvironment());
        featureFlag.setDescription(request.getDescription());
        featureFlag.setProjectId(projectId);
        featureFlag.setEnabled(request.isEnabled());
        featureFlag.setRolloutPercentage(request.getRolloutPercentage());
        featureFlag.setKey(request.getKey());

        return flagRepository.save(featureFlag);
    }

    public List<FeatureFlag> getFlagsByProjectAndEvironment(Long projectId, String environment) {
        return flagRepository.findByProjectIdAndEnvironment(projectId, environment);
    }

    public FeatureFlag getFlagById(Long id) {
        return flagRepository.findById(id).orElseThrow(() -> new RuntimeException("Flag not found"));
    }

    public FeatureFlag updateFlag(Long id, UpdateFlagRequest request) {
        FeatureFlag flag = getFlagById(id);
        flag.setDescription(request.getDescription());
        flag.setEnabled(request.isEnabled());
        flag.setRolloutPercentage(request.getRolloutPercentage());
        return flagRepository.save(flag);
    }

    public void deleteFlag(Long id) {
        FeatureFlag flag = getFlagById(id);
        flagRepository.delete(flag);
    }

    public boolean isFlagEnabledForDevice(FeatureFlag flag, String deviceId) {
        if (!flag.isEnabled()) return false;
        if (flag.getRolloutPercentage() == 100) return true;
        if (flag.getRolloutPercentage() == 0) return false;

        String seed = deviceId + ":" + flag.getKey();
        int hash = Math.abs(seed.hashCode());
        int bucket = hash % 100;

        return bucket < flag.getRolloutPercentage();
    }
}
