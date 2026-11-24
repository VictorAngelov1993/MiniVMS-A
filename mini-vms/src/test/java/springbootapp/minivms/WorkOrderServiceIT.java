package springbootapp.minivms;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springbootapp.minivms.model.entities.enums.WorkOrderStatus;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.workitems.JobPosting;
import springbootapp.minivms.model.entities.workitems.JobSeeker;
import springbootapp.minivms.model.entities.workitems.WorkOrder;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.workitems.JobPostingRepository;
import springbootapp.minivms.repositories.workitems.JobSeekerRepository;
import springbootapp.minivms.repositories.workitems.WorkOrderRepository;
import springbootapp.minivms.services.workitemsservices.WorkOrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class WorkOrderServiceIT {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private BuyerRepository buyerRepository;



    @Test
    void testCloseWorkOrder() {
        Supplier supplier = this.createSupplier();
        this.supplierRepository.save(supplier);
        Buyer buyer = this.createBuyer();
        this.buyerRepository.save(buyer);
        JobSeeker jobSeeker = this.createJobSeeker(supplier);
        this.jobSeekerRepository.save(jobSeeker);
        JobPosting jobPosting = this.createJobPosing(buyer);
        this.jobPostingRepository.save(jobPosting);
        WorkOrder workOrder = this.createWorkOrder(jobPosting, jobSeeker, supplier);
        this.workOrderRepository.save(workOrder);

        this.workOrderService.closeWorkOrderByWorkOrderId("WO-999");

        WorkOrder updated = workOrderRepository.getWorkOrderByWorkOrderId("WO-999");

        assertEquals(
                WorkOrderStatus.CLOSED,
                updated.getStatus()
        );
    }

    private Supplier createSupplier() {
        Supplier supplier = new Supplier();
        supplier.setUsername("SupplierUsername");
        supplier.setFirstName("SupplierFirstName");
        supplier.setLastName("SupplierLastName");
        supplier.setEmail("Supplier@Email.com");
        supplier.setPassword("SupplierPassword");
        supplier.setActive(true);
        supplier.setCreatedOn(LocalDateTime.now().minusDays(5));
        return supplier;
    }

    private Buyer createBuyer() {
        Buyer buyer = new Buyer();
        buyer.setUsername("BuyerUsername");
        buyer.setFirstName("BuyerFirstName");
        buyer.setLastName("BuyerLastName");
        buyer.setPassword("BuyerPassword");
        buyer.setEmail("Buyer@Email.com");
        buyer.setActive(true);
        buyer.setCreatedOn(LocalDateTime.now().minusDays(5));
        return buyer;
    }

    private JobSeeker createJobSeeker(Supplier supplier) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setJobSeekerId("JS-999");
        jobSeeker.setFirstName("JSFirstName");
        jobSeeker.setLastName("JSLastName");
        jobSeeker.setSupplier(supplier);
        return jobSeeker;
    }

    private JobPosting createJobPosing(Buyer buyer) {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setJobPostingId("JP-999");
        jobPosting.setStartDate(LocalDate.now().minusDays(1));
        jobPosting.setEndDate(LocalDate.now().plusDays(30));
        jobPosting.setBuyer(buyer);
        return jobPosting;
    }

    private WorkOrder createWorkOrder(JobPosting jobPosting, JobSeeker jobSeeker, Supplier supplier) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setJobPosting(jobPosting);
        workOrder.setWorkOrderId("WO-999");
        workOrder.setJobSeeker(jobSeeker);
        workOrder.setStartDate(jobPosting.getStartDate());
        workOrder.setEndDate(jobPosting.getEndDate());
        workOrder.setSupplier(supplier);
        workOrder.setStatus(WorkOrderStatus.ACTIVE);
        return workOrder;
    }

}
