package springbootapp.minivms.model.dto.persondto;

import springbootapp.minivms.model.entities.enums.Role;


// This DTO is used for the Registration Page
public class PersonRegistrationDto {

    // I have tried to work with the jakarta.validation.constraints.* to validate the data via Annotations in the DTO
    // ,but it's not working well with the JS that I have, so I
    // have moved all the validation in the Service.
    private  String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private Role role;
    // The Work Order id is used only when the Role is Worker.
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
