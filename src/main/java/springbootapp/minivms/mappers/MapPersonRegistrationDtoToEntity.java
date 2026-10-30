package springbootapp.minivms.mappers;

import org.springframework.stereotype.Component;
import springbootapp.minivms.model.dto.persondto.PersonRegistrationDto;
import springbootapp.minivms.model.entities.persons.*;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// This mapper maps the Registration Dto to the User entity.
@Component
public class MapPersonRegistrationDtoToEntity {

    public Buyer registrationDtoToBuyer(PersonRegistrationDto dto) {
        Buyer buyer = new Buyer();
        addCommonFields(dto, buyer);
        return buyer;
    }

    public Supplier registrationDtoToSupplier(PersonRegistrationDto dto) {
        Supplier supplier = new Supplier();
        addCommonFields(dto, supplier);
        return supplier;
    }

    public Worker registrationDtoToWorker(PersonRegistrationDto dto) {
        Worker worker = new Worker();
        addCommonFields(dto, worker);
        return worker;
    }

    public void addCommonFields(PersonRegistrationDto dto, AbstractPerson user) {
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setActive(true);
        user.setCreatedOn(LocalDateTime.now());
    }
}
