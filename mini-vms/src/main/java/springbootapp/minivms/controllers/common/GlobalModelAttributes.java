package springbootapp.minivms.controllers.common;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;


@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("user")
    public LoggedUserDto addLoggedUserToModel(@AuthenticationPrincipal LoggedUserDto loggedUser) {
        return loggedUser;
    }


}
