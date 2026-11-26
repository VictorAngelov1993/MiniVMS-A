package springbootapp.minivms.model.dto.workitemdto.jobseeker;

import jakarta.validation.constraints.NotBlank;

public class JobSeekerSubmitDto {
    @NotBlank(message = "Please select a job seeker")
    private String jobSeekerId;
    private String firstName;
    private String lastName;

    public JobSeekerSubmitDto() {

    }

    public String getJobSeekerId() {
        return jobSeekerId;
    }

    public void setJobSeekerId(String jobSeekerId) {
        this.jobSeekerId = jobSeekerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
