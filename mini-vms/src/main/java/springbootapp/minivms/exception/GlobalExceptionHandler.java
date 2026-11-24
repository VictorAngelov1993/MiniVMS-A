package springbootapp.minivms.exception;



import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import springbootapp.minivms.exception.custom.*;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleDefaultExceptions(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexcepted error occurred.");
        return "custom-error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(Exception exception, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", exception.getMessage() != null ? exception.getMessage() : "User Not Found");
        return "custom-error";
    }

    @ExceptionHandler(NotExistingSupplier.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotExistingSupplier(Exception exception, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", exception.getMessage() != null ? exception.getMessage() : "Not Existing Supplier");
        return "custom-error";
    }

    @ExceptionHandler(NotExistingWorker.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotExistingWorkerException(Exception exception, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", exception.getMessage() != null ? exception.getMessage() : "Not Existing Worker");
        return "custom-error";
    }

    @ExceptionHandler(NotExistingWorkOrder.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotExistingWorkOrderException(Exception exception, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", exception.getMessage() != null ? exception.getMessage() : "Not Existing Work Order");
        return "custom-error";
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBudgetNotFoundException(Exception exception, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", exception.getMessage() != null ? exception.getMessage() : "No budget account exists");
        return "custom-error";
    }


}
