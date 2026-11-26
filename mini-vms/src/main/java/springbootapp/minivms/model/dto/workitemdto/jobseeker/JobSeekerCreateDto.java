package springbootapp.minivms.model.dto.workitemdto.jobseeker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class JobSeekerCreateDto {
    @NotBlank(message = "First Name is required")
    @Size(min = 1, max = 50, message = "First Name needs to be between 1 and 50 characters")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    @Size(min = 1, max = 50, message = "Last Name needs to be between 1 and 50 characters")
    private String lastName;
    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 5000, message = "Descriptions should not exceed 5000 characters")
    private String description;
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9]+([._-][A-Za-z0-9]+)*@[A-Za-z]+(-[A-Za-z]+)*(\\.[A-Za-z]+(-[A-Za-z]+)*)+$", message = "Email format should match global standards example@example.com")
    private String email;
    @NotBlank(message = "Phone Number is required")
    @Size(min = 1, max = 50, message = "Phone Number needs to be between 1 and 50 characters")
    private String phoneNumber;

    public JobSeekerCreateDto() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
