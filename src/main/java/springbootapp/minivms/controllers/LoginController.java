package springbootapp.minivms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springbootapp.minivms.model.dto.persondto.PersonLoginDto;
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
        model.addAttribute("personLoginDto", new PersonLoginDto());
        return "login";
    }

}
