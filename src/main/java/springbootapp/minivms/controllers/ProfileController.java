package springbootapp.minivms.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.persondto.ProfileDetailDto;
import springbootapp.minivms.services.personservices.ProfileService;

import java.util.UUID;

@Controller
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String openProfile(HttpSession session, Model model) {

        LoggedUserDto loggedUserDto = (LoggedUserDto) session.getAttribute("loggedUser");
        ProfileDetailDto loggedUserDetails = null;
        try {
            loggedUserDetails = this.profileService.getLoggedUserDetails(loggedUserDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/access-denied";
        }
        model.addAttribute("user", loggedUserDetails);
        return "components/profile";

    }
}
