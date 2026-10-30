package springbootapp.minivms.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.entities.enums.Role;

public class UserTypeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {
        // We are getting the session, create false means that if no session is found spring will not create a session it will return null
        HttpSession session = request.getSession(false);
        // we get the logger user if no logged user found then the object will be null
        LoggedUserDto loggedUser = (session != null) ? (LoggedUserDto) session.getAttribute("loggedUser") : null;
        // we get the URI from the request.
        String uri = request.getRequestURI();

        // if not user is found redirect to login page.
        if(loggedUser == null) {
            response.sendRedirect("/login");
            return false;
        }
        // if a user who is not buyer tries to access buyer pages it will be redirected to the access denied page
        if(uri.startsWith("/buyer") && loggedUser.getRole() != Role.BUYER) {
            response.sendRedirect("/access-denied");
            return false;
        }
        // if a user who is not suppler ^^
        if(uri.startsWith("/supplier") && loggedUser.getRole() != Role.SUPPLIER) {
            response.sendRedirect("/access-denied");
            return false;
        }
        // if a user is not worker ^^
        if(uri.startsWith("/worker") && loggedUser.getRole() != Role.WORKER) {
            response.sendRedirect("/access-denied");
            return false;
        }


        // Below is just a test to see how Cookies behave.
        // TODO can be removed once behavior is observed
        Cookie cookie = new Cookie("CookieFromInterceptor", "thisCookiesIsFromTheInterceptor");
        cookie.setMaxAge(1000);
        response.addCookie(cookie);

        return true;
    }
}
