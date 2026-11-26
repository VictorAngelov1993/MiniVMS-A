package springbootapp.minivms.services.workitemsservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.JobSeekerMapper;
import springbootapp.minivms.model.dto.workitemdto.jobposting.JobPostingDetailsDto;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerSubmitDto;
import springbootapp.minivms.model.entities.workitems.JobPosting;
import springbootapp.minivms.model.entities.workitems.JobSeeker;
import springbootapp.minivms.model.entities.workitems.JobSeekerSubmission;
import springbootapp.minivms.repositories.workitems.JobSeekerSubmissionRepository;

import java.util.List;

@Service
public class JobSeekerSubmissionService {

    private final JobSeekerSubmissionRepository jobSeekerSubmissionRepository;
    private final JobSeekerService jobSeekerService;
    private final JobPostingService jobPostingService;
    private final JobSeekerMapper jobSeekerMapper;

    @Autowired()
    public JobSeekerSubmissionService(JobSeekerSubmissionRepository jobSeekerSubmissionRepository,
                                      JobSeekerService jobSeekerService,
                                      JobPostingService jobPostingService,
                                      JobSeekerMapper jobSeekerMapper) {
        this.jobSeekerSubmissionRepository = jobSeekerSubmissionRepository;
        this.jobSeekerService = jobSeekerService;
        this.jobPostingService = jobPostingService;
        this.jobSeekerMapper = jobSeekerMapper;
    }

    public void createJobSeekerSubmit(JobSeekerSubmitDto jobSeekerSubmitDto, JobPostingDetailsDto jobPostingDetailsDto) {
        JobSeeker jobSeeker = this.jobSeekerService.getJobSeekerById(jobSeekerSubmitDto.getJobSeekerId());
        JobPosting jobPosting = this.jobPostingService.getJobPostingById(jobPostingDetailsDto.getJobPostingId());
        JobSeekerSubmission jobSeekerSubmission = new JobSeekerSubmission();
        jobSeekerSubmission.setJobPosting(jobPosting);
        jobSeekerSubmission.setJobSeeker(jobSeeker);
        this.jobSeekerSubmissionRepository.save(jobSeekerSubmission);
    }

    public List<JobSeekerSubmitDto> getAllJobSeekersSubmittedForThisJobPosting(String jobPostingId) {
        JobPosting jobPosting = this.jobPostingService.getJobPostingById(jobPostingId);
        List <JobSeekerSubmission> submittedJobSeekers = this.jobSeekerSubmissionRepository.findByJobPosting(jobPosting);
        List<JobSeeker> jobSeekers = submittedJobSeekers.stream()
                .map(JobSeekerSubmission::getJobSeeker)
                .toList();
        return this.jobSeekerMapper.mapJobSeekersToJobSeekerSubmitDto(jobSeekers);

    }
}
