package springbootapp.minivms.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        // TODO maybe the go to home button should send the user to the dashboard intend of login page
        return "access-denied";
    }

    // This is a test cookie and does not have any actual purpose.
    // It will only work if we type http://localhost:8080/set-cookies
    // TODO can be removed once behavior is observed
    @GetMapping("/set-cookies")
    public String setCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie("Manual Cookie", "onlyOn/set-cookiesURI");
        // the setPath tells which uri's will get the cookie.
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return "redirect:/login";
    }


}
