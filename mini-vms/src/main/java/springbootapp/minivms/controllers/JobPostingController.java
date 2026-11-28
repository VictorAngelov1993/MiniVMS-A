package springbootapp.minivms.controllers;



import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.jobposting.JobPostingCreateDto;
import springbootapp.minivms.model.dto.workitemdto.jobposting.JobPostingDetailsDto;
import springbootapp.minivms.model.entities.workitems.JobPosting;
import springbootapp.minivms.services.workitemsservices.JobPostingService;

import java.util.List;


@Controller
public class JobPostingController {

    private JobPostingService jobPostingService;


    @Autowired
    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping("/buyer/job-postings")
    public String showBuyerJobPostings(@AuthenticationPrincipal LoggedUserDto loggedUser, Model model) {

        List<JobPosting> jobPostings = this.jobPostingService.getJobPostingForBuyer(loggedUser.getUuid());
        model.addAttribute("jobPostings", jobPostings);

        return "buyer/job-postings";
    }

    @GetMapping("/buyer/create-job-posting")
    public String createJobPosting(Model model) {
        if(!model.containsAttribute("jobPostingDto")) {
            model.addAttribute("jobPostingDto", new JobPostingCreateDto());
        }
        return "buyer/create-job-posting";
    }

    @PostMapping("/buyer/create-job-posting")
    public String processCreateJobPosting(@Valid @ModelAttribute("jobPostingDto") JobPostingCreateDto dto,
                                          BindingResult bindingResult,
                                          @AuthenticationPrincipal LoggedUserDto loggedUser,
                                          Model model) {

        if(bindingResult.hasErrors()) {
            return "buyer/create-job-posting";
        }

        try {
            this.jobPostingService.createJobPosting(dto, loggedUser.getUuid());
            return "redirect:/buyer/job-postings";
        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "/buyer/create-job-posting";
        }

    }
    @PreAuthorize("@buyerWorkItemSecurity.isOwned(#jobPostingId, authentication.principal.uuid)")
    @GetMapping("/buyer/job-postings/{jobPostingId}")
    public String showBuyerJobPostingDetails(@PathVariable String jobPostingId,
                                             @AuthenticationPrincipal LoggedUserDto loggedUser,
                                             Model model) {

        JobPostingDetailsDto jobPosting = this.jobPostingService.getJobPostingCreateDtoById(jobPostingId);
        model.addAttribute("posting", jobPosting);
        return "buyer/job-posting-details";
    }


    @GetMapping("/supplier/job-postings")
    public String showSupplierJobPostings(@AuthenticationPrincipal LoggedUserDto loggedUser, Model model) {

        List<JobPosting> supplierJobPostings = this.jobPostingService.getJobPostingForSupplier();
        model.addAttribute("jobPostings", supplierJobPostings);


        return "supplier/job-postings";
    }
    @GetMapping("/supplier/job-postings/{jobPostingId}")
    public String showSupplierJobPostingDetails(@PathVariable String jobPostingId,
                                                @AuthenticationPrincipal LoggedUserDto loggedUser,
                                                Model model) {
        JobPostingDetailsDto jobPosting = this.jobPostingService.getJobPostingCreateDtoById(jobPostingId);
        model.addAttribute("posting", jobPosting);
        return "supplier/job-posting-details";
    }
}
