package springbootapp.minivms.model.entities.persons;

public interface Person {
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setEmail(String email);
    String getFullName();
    String getEmail();
}
