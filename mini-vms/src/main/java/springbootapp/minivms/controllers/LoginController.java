package springbootapp.minivms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springbootapp.minivms.model.dto.persondto.PersonLoginDto;
import springbootapp.minivms.services.personservices.PersonAuthenticationService;

@Controller
public class LoginController {

    private final PersonAuthenticationService personAuthenticationService;

    @Autowired
    public LoginController(PersonAuthenticationService personAuthenticationService) {
        this.personAuthenticationService = personAuthenticationService;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error,
                                Model model) {
        model.addAttribute("personLoginDto", new PersonLoginDto());

        if(error != null) {
            model.addAttribute("errorMessage" , "Invalid Username or Password");
        }

        return "login";
    }

}
