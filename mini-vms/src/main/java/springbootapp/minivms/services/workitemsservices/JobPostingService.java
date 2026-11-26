package springbootapp.minivms.services.workitemsservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.model.dto.workitemdto.jobposting.JobPostingCreateDto;
import springbootapp.minivms.model.dto.workitemdto.jobposting.JobPostingDetailsDto;
import springbootapp.minivms.model.entities.enums.JobPostingStatus;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.workitems.JobPosting;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.workitems.JobPostingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final BuyerRepository buyerRepository;
    private static final Logger log = LoggerFactory.getLogger(JobPostingService.class);

    @Autowired
    public JobPostingService(JobPostingRepository jobPostingRepository,
                             BuyerRepository buyerRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.buyerRepository = buyerRepository;
    }

    public List<JobPosting> getJobPostingForBuyer(UUID loggedUserUuid) {
        return this.jobPostingRepository.getJobPostingForBuyer(loggedUserUuid);
    }

    public List<JobPosting> getJobPostingForSupplier() {
        return this.jobPostingRepository.getJobPostingForSupplier();
    }

    public void createJobPosting(JobPostingCreateDto dto, UUID buyerUuid) {
        log.info("Starting the process of creating Job Posting");
        Optional<Buyer> buyer = this.buyerRepository.getBuyerByUuid(buyerUuid);
        Buyer buyerUser = buyer.orElseThrow( () -> new IllegalStateException("Invalid logged User"));
        if(dto.getEndDate().isBefore(dto.getStartDate())) {
            log.warn("User entered end date before start date");
            throw new IllegalArgumentException("End Date cannot be before Start Date");
        }
        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle(dto.getTitle());
        jobPosting.setDescription(dto.getDescription());
        jobPosting.setStartDate(dto.getStartDate());
        jobPosting.setEndDate(dto.getEndDate());
        jobPosting.setPayRate(dto.getPayRate());
        jobPosting.setStatus(JobPostingStatus.OPEN);
        jobPosting.setJobPostingId(this.generateJobPostingId());
        jobPosting.setBuyer(buyerUser);
        this.jobPostingRepository.save(jobPosting);
        log.info("Job Posting successfully created");

    }

    private String generateJobPostingId() {
        long countJobPostings = this.jobPostingRepository.count();
        return String.format("JP-%03d", countJobPostings + 1);

    }

    public int countJobPostingsForBuyer(Buyer buyer) {
        return this.jobPostingRepository.countByBuyer(buyer);
    }


    public JobPostingDetailsDto getJobPostingCreateDtoById(String jobPostingId) {
        JobPostingDetailsDto jobPostingToReturn = new JobPostingDetailsDto();
        JobPosting jobPosting = this.jobPostingRepository.getJobPostingByJobPostingId(jobPostingId);
        jobPostingToReturn.setTitle(jobPosting.getTitle());
        jobPostingToReturn.setStatus(jobPosting.getStatus());
        jobPostingToReturn.setDescription(jobPosting.getDescription());
        jobPostingToReturn.setStartDate(jobPosting.getStartDate());
        jobPostingToReturn.setEndDate(jobPosting.getEndDate());
        jobPostingToReturn.setPayRate(jobPosting.getPayRate());
        jobPostingToReturn.setJobPostingId(jobPostingId);
        return jobPostingToReturn;

    }

    public JobPosting getJobPostingById(String jobPostingId) {
        return this.jobPostingRepository.getJobPostingByJobPostingId(jobPostingId);
    }

}
