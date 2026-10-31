package springbootapp.minivms.model.dto.persondto;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springbootapp.minivms.model.entities.enums.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


// Tomcat (and Spring Boot DevTools) sometimes serialize the session to disk when the app restarts.
//If the object inside the session is not serializable, you get errors. That's why the logged user needs to implement the serializable

// This DTO is used after the User has successfully logged in.
public class LoggedUserDto implements Serializable, UserDetails {

    //When a class implements Serializable, Java needs a way to know:
    //
    //“Is the version of this class the same as the version used when the object was serialized?”
    //
    //So Java assigns a version number to the class.
    //If you don’t define it, Java generates one automatically based on: Fields methods etc.
    //So even a tiny change (adding a field, renaming something) changes the generated UID → old serialized objects become invalid → Tomcat throws errors.
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private String fullName;
    private String email;
    private Role role;
    // The header.html requires this field to know where to send the user when they click on the home button
    // <a class="navbar-brand app-logo" href="/static" th:href="@{${user != null} ? ${user.roleHomeUrl} : '/'}"
    private String roleHomeUrl;
    private String password;
    private String username;

    public LoggedUserDto() {
    }

    public String getFullName() {
        return fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRoleHomeUrl() {
        return roleHomeUrl;
    }

    public void setRoleHomeUrl(String roleHomeUrl) {
        this.roleHomeUrl = roleHomeUrl;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
