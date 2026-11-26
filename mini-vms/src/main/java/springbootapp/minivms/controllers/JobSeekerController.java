package springbootapp.minivms.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.jobposting.JobPostingDetailsDto;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerCreateDto;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerSubmitDto;
import springbootapp.minivms.model.entities.workitems.JobSeeker;
import springbootapp.minivms.services.workitemsservices.JobPostingService;
import springbootapp.minivms.services.workitemsservices.JobSeekerService;
import springbootapp.minivms.services.workitemsservices.JobSeekerSubmissionService;

import java.util.List;

@Controller
public class JobSeekerController {

    private JobSeekerService jobSeekerService;
    private JobPostingService jobPostingService;
    private JobSeekerSubmissionService jobSeekerSubmissionService;

    @Autowired
    public JobSeekerController(JobSeekerService jobSeekerService, JobPostingService jobPostingService,
    JobSeekerSubmissionService jobSeekerSubmissionService) {
        this.jobSeekerService = jobSeekerService;
        this.jobPostingService = jobPostingService;
        this.jobSeekerSubmissionService = jobSeekerSubmissionService;
    }

    @GetMapping("/supplier/job-seekers")
    public String loadJobSeekers(@AuthenticationPrincipal LoggedUserDto loggedUser, Model model) {

        List<JobSeeker> jobSeekers = this.jobSeekerService.getAllJobSeekersForTheSupplier(loggedUser.getUuid());
        model.addAttribute("jobSeekers", jobSeekers);

        return "supplier/job-seekers";
    }

    @GetMapping("supplier/create-job-seeker")
    public String showCreateJobSeeker(Model model) {
        model.addAttribute("JobSeekerDto", new JobSeekerCreateDto());
        return "supplier/create-job-seeker";
    }

    @PostMapping("supplier/create-job-seeker")
    public String createJobSeeker(@AuthenticationPrincipal LoggedUserDto loggedUser,
                                  @ModelAttribute("jobSeekerDto") JobSeekerCreateDto jobSeekerCreateDto
                                  ) {

        this.jobSeekerService.createJobSeeker(jobSeekerCreateDto, loggedUser.getUuid());
        return "redirect:/supplier/job-seekers";

    }

    @GetMapping("/supplier/submit-job-seeker")
    public String viewSubmitJobSeeker(@RequestParam String jobPostingId,
                                      @AuthenticationPrincipal LoggedUserDto loggedUser,
                                  Model model) {


        List<JobSeekerSubmitDto> jobSeekers = this.jobSeekerService.getAllJobSeekersSuitableForSubmission(loggedUser.getUuid());
        JobPostingDetailsDto jobPosting = this.jobPostingService.getJobPostingCreateDtoById(jobPostingId);

        model.addAttribute("jobSeekers", jobSeekers);
        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute("jobPostingId", jobPostingId);

        return "/supplier/submit-job-seeker";

    }

    @PostMapping("/supplier/submit-job-seeker")
    public String submitJobSeeker(
            @RequestParam("jobSeekerId") String jobSeekerId,
            @RequestParam("jobPostingId") String jobPostingId,
            Model model) {


        JobPostingDetailsDto jobPosting = this.jobPostingService.getJobPostingCreateDtoById(jobPostingId);
        JobSeekerSubmitDto jobSeeker = this.jobSeekerService.getJobSeekerSubmitDtoById(jobSeekerId);

        this.jobSeekerSubmissionService.createJobSeekerSubmit(jobSeeker, jobPosting);

        return "redirect:/supplier/job-postings";
    }

}
