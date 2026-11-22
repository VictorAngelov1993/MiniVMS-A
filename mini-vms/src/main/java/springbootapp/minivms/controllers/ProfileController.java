package springbootapp.minivms.controllers;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.persondto.ProfileDetailDto;
import springbootapp.minivms.model.dto.persondto.ProfileEditDto;
import springbootapp.minivms.services.personservices.ProfileService;


@Controller
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String openProfile(@AuthenticationPrincipal LoggedUserDto loggedUser, Model model) {

        ProfileDetailDto loggedUserDetails = null;
        try {
            loggedUserDetails = this.profileService.getLoggedUserDetails(loggedUser);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/access-denied";
        }
        model.addAttribute("user", loggedUserDetails);
        return "components/profile";
    }

    @GetMapping("/profile/edit")
    public String openProfileEdit(Model model) {
        model.addAttribute("profileEditDto", new ProfileEditDto());
        return "components/profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal LoggedUserDto loggedUser,
                              @ModelAttribute("profileEditDto") ProfileEditDto profileEditDto) {

        this.profileService.editProfile(loggedUser, profileEditDto);
        return "redirect:/profile";
    }
}
