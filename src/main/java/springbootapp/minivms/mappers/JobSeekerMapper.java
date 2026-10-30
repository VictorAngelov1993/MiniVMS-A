package springbootapp.minivms.mappers;

import org.springframework.stereotype.Component;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerCreateDto;
import springbootapp.minivms.model.dto.workitemdto.jobseeker.JobSeekerSubmitDto;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.workitems.JobSeeker;

import java.util.List;


@Component
public class JobSeekerMapper {


    public JobSeeker mapJobSeekerCreateDtoToJobSeekerEntity(JobSeekerCreateDto jobSeekerCreateDto, Supplier supplier) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setSupplier(supplier);
        jobSeeker.setFirstName(jobSeekerCreateDto.getFirstName());
        jobSeeker.setLastName(jobSeekerCreateDto.getLastName());
        jobSeeker.setDescription(jobSeekerCreateDto.getDescription());
        jobSeeker.setEmail(jobSeekerCreateDto.getEmail());
        jobSeeker.setPhoneNumber(jobSeekerCreateDto.getPhoneNumber());
        return jobSeeker;
    }

    public List<JobSeekerSubmitDto> mapJobSeekersToJobSeekerSubmitDto(List<JobSeeker> jobSeekers) {
        return jobSeekers.stream().map(this::mapJobSeekerToJobSeekerDto).toList();
    }

    public JobSeekerSubmitDto mapJobSeekerToJobSeekerDto(JobSeeker jobSeeker) {
        JobSeekerSubmitDto jobSeekerSubmitDto = new JobSeekerSubmitDto();
        jobSeekerSubmitDto.setJobSeekerId(jobSeeker.getJobSeekerId());
        jobSeekerSubmitDto.setFirstName(jobSeeker.getFirstName());
        jobSeekerSubmitDto.setLastName(jobSeeker.getLastName());
        return jobSeekerSubmitDto;
    }

}
