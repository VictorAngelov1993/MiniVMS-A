package springbootapp.minivms.model.dto.persondto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import springbootapp.minivms.model.entities.enums.Role;



public class PersonRegistrationDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private  String username;
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9]+([._-][A-Za-z0-9]+)*@[A-Za-z]+(-[A-Za-z]+)*(\\.[A-Za-z]+(-[A-Za-z]+)*)+$", message = "Email format should match global standards example@example.com")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 100, message = "Password must be at least 5 characters")
    private String password;
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
    @NotNull(message = "Role is required")
    private Role role;
    private String workOrderId;

    public PersonRegistrationDto() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getWorkOrder() {
        return workOrderId;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrderId = workOrder;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
}
