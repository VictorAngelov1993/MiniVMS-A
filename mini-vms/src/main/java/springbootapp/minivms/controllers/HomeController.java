package springbootapp.minivms.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/set-cookies")
    public String setCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie("This is a set cookie", "onlyOn/set-cookiesURI");
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return "redirect:/login";
    }


}
