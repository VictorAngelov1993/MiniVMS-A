package springbootapp.minivms.services.workitemsservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.exception.custom.NotExistingSupplier;
import springbootapp.minivms.mappers.JobSeekerMapper;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerCreateDto;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerSubmitDto;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.workitems.JobSeeker;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.workitems.JobSeekerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobSeekerService {

    private final JobSeekerRepository jobSeekerRepository;
    private final SupplierRepository supplierRepository;
    private final JobSeekerMapper jobSeekerMapper;
    private static final Logger log = LoggerFactory.getLogger(JobSeekerService.class);

    @Autowired
    public JobSeekerService(JobSeekerRepository jobSeekerRepository,
                            SupplierRepository supplierRepository,
                            JobSeekerMapper jobSeekerMapper) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.supplierRepository = supplierRepository;
        this.jobSeekerMapper = jobSeekerMapper;
    }

    public List<JobSeeker> getAllJobSeekersForTheSupplier(UUID supplierUuid) {
        Optional <Supplier> loggedSupplier = this.supplierRepository.getSupplierByUuid(supplierUuid);
        Supplier supplier = loggedSupplier.orElseThrow(() -> new NotExistingSupplier("Not Existing Supplier"));

        return this.jobSeekerRepository.getJobSeekerBySupplier(supplier);
    }

    public void createJobSeeker(JobSeekerCreateDto jobSeekerCreateDto, UUID supplierUuid) {
        log.info("Starting the process of creating Job Seeker");
        Optional <Supplier> loggedSupplier = this.supplierRepository.getSupplierByUuid(supplierUuid);
        Supplier supplier = loggedSupplier.orElseThrow(() ->{
            log.warn("Supplier with UUID {} does not exist", supplierUuid);
            return new NotExistingSupplier("Not Existing Supplier");
        });

        log.info("Supplier with UUID {} is creating the Job Seeker", supplierUuid);

        JobSeeker jobSeeker = this.jobSeekerMapper.mapJobSeekerCreateDtoToJobSeekerEntity(jobSeekerCreateDto, supplier);
        String jobSeekerId = this.generateJobSeekerId();
        jobSeeker.setJobSeekerId(jobSeekerId);

        this.jobSeekerRepository.save(jobSeeker);
        log.info("Job Seeker with id {} was created", jobSeekerId);
    }

    public List<JobSeekerSubmitDto> getAllJobSeekersSuitableForSubmission(UUID supplierUuid) {
        List<JobSeeker> jobSeekers = this.jobSeekerRepository.getAllBySupplier_Uuid(supplierUuid);
        return this.jobSeekerMapper.mapJobSeekersToJobSeekerSubmitDto(jobSeekers);
    }

    private String generateJobSeekerId() {
        long coundJobSeekers = this.jobSeekerRepository.count();
        return String.format("JS-%03d", coundJobSeekers + 1);
    }

    public JobSeekerSubmitDto getJobSeekerSubmitDtoById(String jobSeekerId) {

        JobSeeker jobSeeker = this.jobSeekerRepository.getJobSeekerByJobSeekerId(jobSeekerId);
        return this.jobSeekerMapper.mapJobSeekerToJobSeekerDto(jobSeeker);
    }

    public JobSeeker getJobSeekerById(String jobSeekerId) {
        return this.jobSeekerRepository.getJobSeekerByJobSeekerId(jobSeekerId);
    }

    public long countTheSupplierJobSeekers(Supplier supplier) {
        return this.jobSeekerRepository.countBySupplier(supplier);
    }


}
