package springbootapp.minivms.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springbootapp.minivms.model.dto.persondto.PersonRegistrationDto;
import springbootapp.minivms.services.personservices.RegistrationService;

// This controller is used for the Registration Page
@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // When we use th:object we need to provide the object in the model.
        // therefore we must pass the DTO in the GET method.
        model.addAttribute("personRegistrationDto", new PersonRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("personRegistrationDto") PersonRegistrationDto personRegistrationDto,
            BindingResult bindingResult,
            Model model) {

        // validates that the password and confirmed password are match
        if (!personRegistrationDto.getPassword().equals(personRegistrationDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }
        // If there are any errors stored in the bindingResult return the Registration page and do not trigger the registration method.
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            this.registrationService.registerUser(personRegistrationDto);
            return "redirect:/login";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "register";
        }

    }




}
