package springbootapp.minivms.services.personservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.model.dto.persondto.SupplierDashboardDto;
import springbootapp.minivms.model.entities.enums.WorkOrderStatus;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;
import springbootapp.minivms.repositories.workitems.WorkOrderRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final BuyerRepository buyerRepository;
    private final WorkerRepository workerRepository;
    private final WorkOrderRepository workOrderRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository,
                           BuyerRepository buyerRepository,
                           WorkerRepository workerRepository,
                           WorkOrderRepository workOrderRepository) {
        this.supplierRepository = supplierRepository;
        this.buyerRepository = buyerRepository;
        this.workerRepository = workerRepository;
        this.workOrderRepository = workOrderRepository;
    }

    public void register(Supplier supplier) {
        //Below checks if a different user role has the same Username
        if(this.supplierRepository.countAllByUsername(supplier.getUsername()) == 1
        || this.buyerRepository.countAllByUsername(supplier.getUsername()) == 1
        || this.workerRepository.countAllByUsername(supplier.getUsername()) == 1) {
            throw new IllegalArgumentException("Username already exist");
        }
        this.supplierRepository.save(supplier);
    }

    public Optional<Supplier> getSupplierByUuid(UUID uuid) {
        return this.supplierRepository.getSupplierByUuid(uuid);
    }

    public long countJobSeekers() {
        return this.supplierRepository.count();
    }

    public long countSubmittedJobSeekers(Supplier supplier) {
        return this.workOrderRepository.countAllBySupplier(supplier);
    }

    public long countActiveJobSeekers(Supplier supplier) {
        return this.workOrderRepository.countWorkOrderBySupplierAndStatus(supplier, WorkOrderStatus.ACTIVE);
    }

}
