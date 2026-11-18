package springbootapp.minivms.services.personservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.MapAbstractPersonToLoggedUserDto;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.entities.persons.AbstractPerson;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;

import java.util.Optional;

@Service
public class PersonAuthenticationService {
    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapAbstractPersonToLoggedUserDto mapAbstractPersonToLoggedUserDto;

    // This service is used for the Login process.
    @Autowired
    public PersonAuthenticationService(BuyerRepository buyerRepository,
                                       SupplierRepository supplierRepository,
                                       WorkerRepository workerRepository,
                                       PasswordEncoder passwordEncoder,
                                       MapAbstractPersonToLoggedUserDto mapAbstractPersonToLoggedUserDto) {
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.workerRepository = workerRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapAbstractPersonToLoggedUserDto = mapAbstractPersonToLoggedUserDto;
    }

    public LoggedUserDto authenticate(String username, String rawPassword) {

        // Checks if the Username exists if yes it will return the person if not it will throw exception
        AbstractPerson abstractPerson = this.findPersonByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Username or Password"));

        // Then checks if the password is correct. If not correct it will throw exception
        if(!passwordEncoder.matches(rawPassword, abstractPerson.getPassword())) {
            throw new IllegalArgumentException("Invalid Username or Password");
        }
        // Map the abstract person to logged user dto and return the Dto.
        return this.mapAbstractPersonToLoggedUserDto.getLoggedUser(abstractPerson);
    }


    private Optional<AbstractPerson> findPersonByUsername(String username) {
        return this.buyerRepository.getBuyerByUsername(username).map(buyer -> (AbstractPerson) buyer)
                .or(() -> this.supplierRepository.getSupplierByUsername(username).map(supplier -> (AbstractPerson) supplier))
                .or(() -> this.workerRepository.getWorkerByUsername(username).map(worker -> (AbstractPerson) worker));
    }
}
