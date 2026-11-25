package springbootapp.minivms.config.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Configuration
public class FeignSecurityConfig {
    private final FeignJwtService feignJwtService;

    public FeignSecurityConfig(FeignJwtService feignJwtService) {
        this.feignJwtService = feignJwtService;
    }

    @Bean
    public RequestInterceptor jwtRequestInterceptor() {
        return (RequestTemplate template) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String subject = (auth != null && auth.getName() != null) ? auth.getName() : "minivms-service";

            List<String> roles = (auth != null && !auth.getAuthorities().isEmpty())
                    ? auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                    .toList()
                    : List.of("BUYER");

            String token = feignJwtService.generateServiceToken(subject, roles);
            template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        };
    }
}
