package springbootapp.minivms.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;


@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        String targetUrl = "/";

        if (roles.contains("ROLE_BUYER")) {
            targetUrl = "/buyer/dashboard";
        } else if (roles.contains("ROLE_SUPPLIER")) {
            targetUrl = "/supplier/dashboard";
        } else if (roles.contains("ROLE_WORKER")) {
            targetUrl = "/worker/dashboard";
        }
        log.info("User {} with role {} was authenticated and will be redirected to {}", authentication.getName(), roles.stream().findFirst(), targetUrl);
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
