package springbootapp.minivms.model.dto.persondto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import springbootapp.minivms.model.entities.enums.Role;



public class PersonRegistrationDto {

    @Size(min = 3, message = "Username should be at least 3 characters")
    private  String username;
    @NotNull(message = "Name should be at least 1 character")
    private String firstName;
    @NotNull(message = "Name should be at least 1 character")
    private String lastName;
    @Pattern(regexp = "^[A-Za-z0-9]+([._-][A-Za-z0-9]+)*@[A-Za-z]+(-[A-Za-z]+)*(\\.[A-Za-z]+(-[A-Za-z]+)*)+$", message = "Email format should match global standards example@example.com")
    private String email;
    @Size(min = 5, message = "Password should be at least 5 characters")
    private String password;
    private String confirmPassword;
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
