package springbootapp.minivms.model.dto.persondto;


import springbootapp.minivms.model.entities.enums.Role;

public class ProfileDetailDto {
    private String roleHomeUrl;
    private String fullName;
    private String email;
    private Role role;

    public ProfileDetailDto() {}

    public String getRoleHomeUrl() {
        return roleHomeUrl;
    }

    public void setRoleHomeUrl(String roleHomeUrl) {
        this.roleHomeUrl = roleHomeUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
