package springbootapp.minivms.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.persondto.PersonLoginDto;
import springbootapp.minivms.model.entities.enums.Role;
import springbootapp.minivms.services.personservices.PersonAuthenticationService;

// This controller os used for the Login page
@Controller
public class LoginController {

    private final PersonAuthenticationService personAuthenticationService;

    @Autowired
    public LoginController(PersonAuthenticationService personAuthenticationService) {
        this.personAuthenticationService = personAuthenticationService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // todo add success message after registration
        //  and more advanced dynamic data the username to be prefilled when registration is successful

        model.addAttribute("personLoginDto", new PersonLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("personLoginDto") PersonLoginDto personLoginDto,
                               HttpSession session,
                               Model model,
                               HttpServletResponse response) {
        String username = personLoginDto.getUsername();
        String password = personLoginDto.getPassword();

        // TODO implement the remember me function.


        // Below is just a test to see how Cookies behave.
        // TODO can be removed once behavior is observed
        Cookie cookie = new Cookie("cookieFromLoginController", "createdViaLoginController");
        cookie.setMaxAge(1000);
        response.addCookie(cookie);

        try{

            LoggedUserDto personToLogIn = this.personAuthenticationService.authenticate(username, password);
            // This line puts the logged-in user into the session, so the app can recognize them on every page.
            session.setAttribute("loggedUser", personToLogIn);

            if(personToLogIn.getRole().equals(Role.BUYER)) {
                return "redirect:/buyer/dashboard";
            } else if (personToLogIn.getRole().equals(Role.SUPPLIER)) {
                return "redirect:/supplier/dashboard";
            } else if (personToLogIn.getRole().equals(Role.WORKER)) {
                return "redirect:/worker/dashboard";
            }
            //fallback
            return "redirect:/";

        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "login";
        }

    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // destroys the session
        return "redirect:/login";
    }


}
