package com.analyticsplatform.analytics_platform.auth;

import com.analyticsplatform.analytics_platform.project.ApiKey;
import com.analyticsplatform.analytics_platform.project.ApiKeyRepository;
import com.analyticsplatform.analytics_platform.project.ProjectService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;
    private final ProjectService projectService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().startsWith("/sdk/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-Key");

        if(apiKey == null || apiKey.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String hashedKey = projectService.hashKey(apiKey);
        final Optional<ApiKey> found = apiKeyRepository.findByKeyHashAndActiveTrue(hashedKey);

        if(found.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ApiKey key = found.get();

        // Store projectId and environment so SDK controllers can access them
        request.setAttribute("projectId", key.getProjectId());
        request.setAttribute("environment", key.getEnvironment());

        // Mark as authenticated
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        key.getProjectId(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_SDK"))
                );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
