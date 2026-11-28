package springbootapp.minivms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springbootapp.minivms.config.security.BuyerWorkItemSecurity;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerCardDto;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerDetailDto;
import springbootapp.minivms.services.personservices.WorkerService;

import java.util.List;

@Controller
public class WorkerController {
    private final WorkerService workerService;
    private final BuyerWorkItemSecurity buyerWorkItemSecurity;

    @Autowired
    public WorkerController(WorkerService workerService, BuyerWorkItemSecurity buyerWorkItemSecurity) {
        this.workerService = workerService;
        this.buyerWorkItemSecurity = buyerWorkItemSecurity;
    }

    @GetMapping("buyer/workers")
    public String loadWorkerListForBuyer(@AuthenticationPrincipal LoggedUserDto loggedUser, Model model) {

        List<WorkerCardDto> workers = this.workerService.getAllWorkerCardDto(loggedUser.getUuid());
        model.addAttribute("workers", workers);

        return "buyer/workers";
    }
    @PreAuthorize("@buyerWorkItemSecurity.isOwned(#id, authentication.principal.uuid)")
    @GetMapping("buyer/workers/{id}")
    public String loadWorkerDetails(@PathVariable String id, Model model) {
        WorkerDetailDto workerDetailDto = this.workerService.getWorkerDetailByWorkerId(id);
        model.addAttribute("worker", workerDetailDto);
        return "buyer/worker-details";

    }
}
