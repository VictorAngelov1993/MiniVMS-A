package springbootapp.minivms.config.security;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.MapAbstractPersonToLoggedUserDto;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;
import java.util.Optional;

// This class gets the User who is login in.

@NullMarked
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final WorkerRepository workerRepository;
    private final MapAbstractPersonToLoggedUserDto mapAbstractPersonToLoggedUserDto;
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(BuyerRepository buyerRepository,
                                    SupplierRepository supplierRepository,
                                    WorkerRepository workerRepository,
                                    MapAbstractPersonToLoggedUserDto mapAbstractPersonToLoggedUserDto) {
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.workerRepository = workerRepository;
        this.mapAbstractPersonToLoggedUserDto = mapAbstractPersonToLoggedUserDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get the user who is login in and pass it to Spring Security.
        Optional<Buyer> optionalBuyer = this.buyerRepository.getBuyerByUsername(username);
        if(optionalBuyer.isPresent()) {
            log.info("The User was identified as Buyer");
            return this.mapAbstractPersonToLoggedUserDto.getLoggedUser(optionalBuyer.get());
        }

        Optional<Supplier> supplierOptional = this.supplierRepository.getSupplierByUsername(username);
        if(supplierOptional.isPresent()) {
            log.info("The User was identified as Supplier");
            return this.mapAbstractPersonToLoggedUserDto.getLoggedUser(supplierOptional.get());
        }

        Optional<Worker> workerOptional = this.workerRepository.getWorkerByUsername(username);
        if(workerOptional.isPresent()) {
            log.info("The User was identified as Worker");
            return this.mapAbstractPersonToLoggedUserDto.getLoggedUser(workerOptional.get());
        }
        log.warn("Could not find User with username {}", username);
        throw new UsernameNotFoundException("User with Username " + username + " not found");

    }
}
