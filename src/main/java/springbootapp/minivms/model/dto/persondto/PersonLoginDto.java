package springbootapp.minivms.model.dto.persondto;


// This DTO is used for the Login page
public class PersonLoginDto {
    private String username;
    private String password;

    public PersonLoginDto() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

