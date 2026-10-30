package springbootapp.minivms.repositories.workitems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springbootapp.minivms.model.entities.enums.WorkOrderStatus;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID> {

    List<WorkOrder> getAllByJobPosting_BuyerUuid(UUID jobPostingBuyerUuid);

    int countByJobPosting_Buyer(Buyer jobPostingBuyer);

    Optional<WorkOrder> findWorkOrderByWorkOrderId(String workOrderId);

    long countWorkOrderByWorker(Worker worker);

    @Query("""
        select count(w) from WorkOrder as w where w.jobPosting.buyer.uuid = :buyerUuid and w.worker is not null
""")
    long countWorkersForBuyer(@Param("buyerUuid") UUID buyerUuid);

    long countAllBySupplier(Supplier supplier);

    long countWorkOrderBySupplierAndStatus(Supplier supplier, WorkOrderStatus status);


    WorkOrder getWorkOrderByWorkOrderId(String workOrderId);
}
