package springbootapp.minivms.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String loadJobSeekers(HttpSession session, Model model) {

        LoggedUserDto loggedUser = (LoggedUserDto) session.getAttribute("loggedUser");

        // Get the Job Seekers and add them to the model
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
    public String createJobSeeker(HttpSession session,
                                  @ModelAttribute("jobSeekerDto") JobSeekerCreateDto jobSeekerCreateDto,
                                  Model model) {

        LoggedUserDto loggedUser = (LoggedUserDto) session.getAttribute("loggedUser");

        this.jobSeekerService.createJobSeeker(jobSeekerCreateDto, loggedUser.getUuid());

        return "redirect:/supplier/job-seekers";

    }

    @GetMapping("/supplier/submit-job-seeker")
    public String viewSubmitJobSeeker(@RequestParam String jobPostingId,
                                  HttpSession session,
                                  Model model) {

        LoggedUserDto loggedUser = (LoggedUserDto) session.getAttribute("loggedUser");
        // Get all the Job Seekers that are suitable for submission for the logged Supplier
        List<JobSeekerSubmitDto> jobSeekers = this.jobSeekerService.getAllJobSeekersSuitableForSubmission(loggedUser.getUuid());
        // Get the Job Posting to which we are going to submit the Job Seeker
        JobPostingDetailsDto jobPosting = this.jobPostingService.getJobPostingCreateDtoById(jobPostingId);

        // Add the Job Posting the Job Seeker and the Job Posting id to the model.
        model.addAttribute("jobSeekers", jobSeekers);
        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute("jobPostingId", jobPostingId);

        return "/supplier/submit-job-seeker";

    }

    @PostMapping("/supplier/submit-job-seeker")
    public String submitJobSeeker(
            @RequestParam("jobSeekerId") String jobSeekerId,
            @RequestParam("jobPostingId") String jobPostingId,
            HttpSession session,
            Model model) {

        // I am using already created Dto's because I only need their Id in order to get the actual entity from the DB
        // that's why i decided not to create new dto's but to use already available dto even if the name is a bit wrong.
        JobPostingDetailsDto jobPosting = this.jobPostingService.getJobPostingCreateDtoById(jobPostingId);
        JobSeekerSubmitDto jobSeeker = this.jobSeekerService.getJobSeekerSubmitDtoById(jobSeekerId);

        // Below will create new jobSeekerSubmission. The jobSeekerSubmission class is used a s bridge
        // between Supplier submitted Job Seekers and Buyer submitted Job Postings
        this.jobSeekerSubmissionService.createJobSeekerSubmit(jobSeeker, jobPosting);



        return "redirect:/supplier/job-postings";
    }

}
