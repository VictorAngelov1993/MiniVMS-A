package springbootapp.minivms.controllers.common;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;

// Global controller advice: adds shared model attributes to all controllers.

//Instead of applying rules to just one controller, @ControllerAdvice acts as a global listener.
// When any request hits any @Controller in your app, Spring intercepts it and applies the shared logic inside this class first.

@ControllerAdvice
public class GlobalModelAttributes {

    //Before Spring runs your target controller method, it executes this @ModelAttribute method first.
    // It takes whatever object the method returns and automatically injects it into the page view under the name "user",
    // making it available everywhere without duplicate code.
    @ModelAttribute("user")
    public LoggedUserDto addLoggedUserToModel(HttpSession session) {
        return (LoggedUserDto) session.getAttribute("loggedUser");
    }


}
