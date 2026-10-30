package springbootapp.minivms.config.security;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;

import java.util.Optional;
@NullMarked
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final WorkerRepository workerRepository;

    public CustomUserDetailsService(BuyerRepository buyerRepository,
                                    SupplierRepository supplierRepository,
                                    WorkerRepository workerRepository) {
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.workerRepository = workerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get the user who is login in and pass it to Spring Security.
        Optional<Buyer> optionalBuyer = this.buyerRepository.getBuyerByUsername(username);
        if(optionalBuyer.isPresent()) {
            Buyer buyer = optionalBuyer.get();
            return User.withUsername(buyer.getUsername())
                    .password(buyer.getPassword())
                    .roles("BUYER")
                    .build();
        }

        Optional<Supplier> supplierOptional = this.supplierRepository.getSupplierByUsername(username);
        if(supplierOptional.isPresent()) {
            Supplier supplier = supplierOptional.get();
            return User.withUsername(supplier.getUsername())
                    .password(supplier.getPassword())
                    .roles("SUPPLIER")
                    .build();
        }

        Optional<Worker> workerOptional = this.workerRepository.getWorkerByUsername(username);
        if(workerOptional.isPresent()) {
            Worker worker = workerOptional.get();
            return User.withUsername(worker.getUsername())
                    .password(worker.getPassword())
                    .roles("WORKER")
                    .build();
        }

        throw new UsernameNotFoundException("User with Username " + username + " not found");

    }
}
