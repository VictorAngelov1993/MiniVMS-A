package springbootapp.minivms.mappers;


import org.springframework.stereotype.Component;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.entities.persons.AbstractPerson;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.enums.Role;

// This mapper maps user to the Logged user Dto.
@Component
public class MapAbstractPersonToLoggedUserDto {

    public LoggedUserDto getLoggedUser(AbstractPerson abstractPerson) {
        LoggedUserDto loggedUserDto = new LoggedUserDto();
        loggedUserDto.setUuid(abstractPerson.getUuid());
        loggedUserDto.setFullName(abstractPerson.getFullName());
        loggedUserDto.setEmail(abstractPerson.getEmail());
        if(abstractPerson instanceof Buyer) {
            loggedUserDto.setRole(Role.BUYER);
            loggedUserDto.setRoleHomeUrl("/buyer/dashboard");
        } else if(abstractPerson instanceof Supplier) {
            loggedUserDto.setRole(Role.SUPPLIER);
            loggedUserDto.setRoleHomeUrl("/supplier/dashboard");
        } else if(abstractPerson instanceof Worker)  {
            loggedUserDto.setRole(Role.WORKER);
            loggedUserDto.setRoleHomeUrl("/worker/dashboard");
        }

        return loggedUserDto;
    }

}
