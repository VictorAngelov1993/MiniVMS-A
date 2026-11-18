package springbootapp.minivms.services.workitemsservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private JobSeekerRepository jobSeekerRepository;
    private SupplierRepository supplierRepository;
    private JobSeekerMapper jobSeekerMapper;

    @Autowired
    public JobSeekerService(JobSeekerRepository jobSeekerRepository,
                            SupplierRepository supplierRepository,
                            JobSeekerMapper jobSeekerMapper) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.supplierRepository = supplierRepository;
        this.jobSeekerMapper = jobSeekerMapper;
    }

    public List<JobSeeker> getAllJobSeekersForTheSupplier(UUID supplierUuid) {
        // I will get the Supplier user here instead of the controller
        Optional <Supplier> loggedSupplier = this.supplierRepository.getSupplierByUuid(supplierUuid);
        Supplier supplier = null;
        try {
            supplier = loggedSupplier.orElseThrow(() -> new IllegalStateException("Something went wrong. Contact Support"));
        } catch (Exception exception) {
            // Here I will need some logger to save the logs. The change or throwing the exception is low. So I will leave it for now
        }


        return this.jobSeekerRepository.getJobSeekerBySupplier(supplier);
    }

    public void createJobSeeker(JobSeekerCreateDto jobSeekerCreateDto, UUID supplierUuid) {
        // Get the supplier who is logged in so to map it to the Job Seeker so that we know who created the Job Seeker
        Optional <Supplier> loggedSupplier = this.supplierRepository.getSupplierByUuid(supplierUuid);
        Supplier supplier = null;
        try {
            supplier = loggedSupplier.orElseThrow(() -> new IllegalStateException("Something went wrong. Contact Support"));
        } catch (Exception exception) {
            // Here I will need some logger to save the logs. The change or throwing the exception is low. So I will leave it for now
        }
        JobSeeker jobSeeker = this.jobSeekerMapper.mapJobSeekerCreateDtoToJobSeekerEntity(jobSeekerCreateDto, supplier);

        // generate the jobseeker id and add it.
        String jobSeekerId = this.generateJobSeekerId();
        jobSeeker.setJobSeekerId(jobSeekerId);

        this.jobSeekerRepository.save(jobSeeker);
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
