package springbootapp.minivms.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.jobposting.JobPostingDetailsDto;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerSubmitDto;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderCardDto;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderCreateDto;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderDetailDto;
import springbootapp.minivms.services.workitemsservices.JobPostingService;
import springbootapp.minivms.services.workitemsservices.JobSeekerSubmissionService;
import springbootapp.minivms.services.workitemsservices.WorkOrderService;

import java.util.List;

@Controller
public class WorkOrderController {
    private final WorkOrderService workOrderService;
    private final JobPostingService jobPostingService;
    private final JobSeekerSubmissionService jobSeekerSubmissionService;

    @Autowired
    public WorkOrderController(WorkOrderService workOrderService,
                               JobPostingService jobPostingService,
                               JobSeekerSubmissionService jobSeekerSubmissionService) {
        this.workOrderService = workOrderService;
        this.jobPostingService = jobPostingService;
        this.jobSeekerSubmissionService = jobSeekerSubmissionService;
    }

    @GetMapping("/buyer/workorders")
    public String loadTheWorkOrderList(@AuthenticationPrincipal LoggedUserDto loggedUser,
                                       Model model) {

        List<WorkOrderCardDto> allWorkOrderCards = this.workOrderService.getAllWorkOrdersAsCards(loggedUser.getUuid());
        model.addAttribute("workOrders", allWorkOrderCards);

        return "buyer/workorders";

    }

    @GetMapping("/buyer/create-workorder")
    public String showWorkOrderCreatePage(@RequestParam("jobPostingId") String jobPostingId,
                                          @AuthenticationPrincipal LoggedUserDto loggedUser,
                                          Model model) {

        model.addAttribute("workOrderCreateDto", new WorkOrderCreateDto());
        this.populateCreateWorkOrderModel(jobPostingId, model);

        return "buyer/create-workorder";
    }


    @PostMapping("/buyer/create-workorder")
    public String createWorkOrder(@Valid @ModelAttribute("workOrderCreateDto") WorkOrderCreateDto dto,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal LoggedUserDto loggedUser,
                                  Model model) {

        if(bindingResult.hasErrors() ) {
            this.populateCreateWorkOrderModel(dto.getJobPostingId(), model);
            return "buyer/create-workorder";
        }

        try {
            this.workOrderService.createWorkOrder(dto);
            return "redirect:/buyer/workorders";
        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "/buyer/workorders";
        }
    }

    @GetMapping("buyer/workorders/{id}")
    public String openWorkOrderDetails(@PathVariable("id") String id,
            Model model) {

        WorkOrderDetailDto workOrderDetailDto = this.workOrderService.getWorkOrderForRegisteredWorker(id);
        model.addAttribute("workorder", workOrderDetailDto);


        return "buyer/workorder-details";
    }

    @PostMapping("buyer/workorders/{id}/close")
    public String closeWorkOrder(@PathVariable("id") String workOrderId) {
        this.workOrderService.closeWorkOrderByWorkOrderId(workOrderId);
        return "redirect:/buyer/workorders";
    }

    private void populateCreateWorkOrderModel(String jobPostingId, Model model) {
        if(jobPostingId != null && !jobPostingId.isBlank()) {
            model.addAttribute("jobPosting", this.jobPostingService.getJobPostingCreateDtoById(jobPostingId));
            model.addAttribute("jobSeekers", this.jobSeekerSubmissionService.getAllJobSeekersSubmittedForThisJobPosting(jobPostingId));
        }
        model.addAttribute("generatedWorkOrderId", this.workOrderService.generateWorkOrderId());
    }


}
