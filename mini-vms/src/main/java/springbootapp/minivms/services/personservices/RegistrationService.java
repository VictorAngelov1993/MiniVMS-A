package springbootapp.minivms.services.personservices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.MapPersonRegistrationDtoToEntity;
import springbootapp.minivms.model.dto.persondto.PersonRegistrationDto;
import springbootapp.minivms.model.entities.persons.AbstractPerson;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.WorkOrder;
import springbootapp.minivms.repositories.workitems.WorkOrderRepository;
import springbootapp.minivms.services.workitemsservices.WorkOrderService;

import java.util.List;

@Service
public class RegistrationService {
    private final BuyerService buyerService;
    private final SupplierService supplierService;
    private final WorkerService workerService;
    private final MapPersonRegistrationDtoToEntity mapper;
    private final PasswordEncoder passwordEncoder;
    private final WorkOrderService workOrderService;
    private final WorkOrderRepository workOrderRepository;

    // This service helps with the Registration of Users
    @Autowired
    public RegistrationService(BuyerService buyerService,
                               SupplierService supplierService,
                               WorkerService workerService,
                               MapPersonRegistrationDtoToEntity mapper,
                               PasswordEncoder passwordEncoder,
                               WorkOrderService workOrderService, WorkOrderRepository workOrderRepository) {
        this.buyerService = buyerService;
        this.supplierService = supplierService;
        this.workerService = workerService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.workOrderService = workOrderService;
        this.workOrderRepository = workOrderRepository;
    }

    public void registerUser(PersonRegistrationDto personRegistrationDto) {

        this.validatePersonRegistrationDto(personRegistrationDto);

        // no need for default because the value is coming from a dropdown and will always be one of the tree.
        switch (personRegistrationDto.getRole()) {
            // the .register() method checks if the Username already exists
            case BUYER -> {
                Buyer buyer = this.mapper.registrationDtoToBuyer(personRegistrationDto);
                this.encodePassword(buyer);
                this.buyerService.register(buyer);
            }
            case SUPPLIER ->{
                Supplier supplier = this.mapper.registrationDtoToSupplier(personRegistrationDto);
                this.encodePassword(supplier);
                this.supplierService.register(supplier);
            }
            case WORKER -> {
                // If Invalid WorkOrder id is added in the registration from the below getWorkOrderById will throw the exception
                WorkOrder workerWorkOrder = this.workOrderService.getWorkOrderById(personRegistrationDto.getWorkOrderId());
                Worker worker = this.mapper.registrationDtoToWorker(personRegistrationDto);
                this.encodePassword(worker);
                workerWorkOrder.setWorker(worker);
                worker.getWorkOrders().add(workerWorkOrder);

                this.workerService.register(worker);
                workOrderRepository.save(workerWorkOrder);
            }
        }
    }

    private void encodePassword(AbstractPerson person) {
        String encoded = passwordEncoder.encode(person.getPassword());
        person.setPassword(encoded);
    }

    private void validatePersonRegistrationDto(PersonRegistrationDto personRegistrationDto) {
        String username = personRegistrationDto.getUsername();
        String firstName = personRegistrationDto.getFirstName();
        String lastName = personRegistrationDto.getLastName();
        String email = personRegistrationDto.getEmail();
        String password = personRegistrationDto.getPassword();

        if(username.length() < 3) {
            throw new IllegalArgumentException("Username should be at least 3 characters");
        }
        if(firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("Name should be at least 1 character");
        }
        String regex = "^[A-Za-z0-9]+([._-][A-Za-z0-9]+)*@[A-Za-z]+(-[A-Za-z]+)*(\\.[A-Za-z]+(-[A-Za-z]+)*)+$";
        boolean isEmailValid = email.matches(regex);

        if(!isEmailValid) {
            throw new IllegalArgumentException("Email format should match global standards example@example.com");
        }
        if(password.length() < 5) {
            throw new IllegalArgumentException("Password should be at least 5 characters");
        }
    }


}
