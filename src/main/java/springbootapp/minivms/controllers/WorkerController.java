package springbootapp.minivms.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerCardDto;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerDetailDto;
import springbootapp.minivms.services.personservices.WorkerService;

import java.util.List;

@Controller
public class WorkerController {
    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("buyer/workers")
    public String loadWorkerListForBuyer(HttpSession session, Model model) {
        LoggedUserDto loggedUser = (LoggedUserDto) session.getAttribute("loggedUser");

        List<WorkerCardDto> workers = this.workerService.getAllWorkerCardDto(loggedUser.getUuid());
        model.addAttribute("workers", workers);

        return "buyer/workers";
    }

    @GetMapping("buyer/workers/{id}")
    public String loadWorkerDetails(@PathVariable String id, Model model) {
        WorkerDetailDto workerDetailDto = null;
        try {
             workerDetailDto = this.workerService.getWorkerDetailByWorkerId(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/access-denied";
        }
        model.addAttribute("worker", workerDetailDto);
        return "buyer/worker-details";

    }
}
