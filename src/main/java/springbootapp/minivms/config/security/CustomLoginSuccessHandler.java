package springbootapp.minivms.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

// we use the CustomLoginSuccessHandled to handle the success logins,
// currently I use it only to redirect the user to the correct dashboard
// this class tells the app what to happen after successfully login.
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


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

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
