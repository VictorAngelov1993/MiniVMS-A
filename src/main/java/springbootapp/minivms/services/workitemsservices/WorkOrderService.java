package springbootapp.minivms.services.workitemsservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.WorkOrderMapper;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderCardDto;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderCreateDto;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderDetailDto;
import springbootapp.minivms.model.entities.enums.WorkOrderStatus;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.JobPosting;
import springbootapp.minivms.model.entities.workitems.JobSeeker;
import springbootapp.minivms.model.entities.workitems.WorkOrder;
import springbootapp.minivms.repositories.workitems.JobPostingRepository;
import springbootapp.minivms.repositories.workitems.JobSeekerRepository;
import springbootapp.minivms.repositories.workitems.TimeSheetRepository;
import springbootapp.minivms.repositories.workitems.WorkOrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMapper workOrderMapper;
    private final JobPostingRepository jobPostingRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final TimeSheetRepository timeSheetRepository;


    @Autowired
    public WorkOrderService(WorkOrderRepository workOrderRepository,
                            WorkOrderMapper workOrderMapper,
                            JobPostingRepository jobPostingRepository,
                            JobSeekerRepository jobSeekerRepository,
                            TimeSheetRepository timeSheetRepository
                            ) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderMapper = workOrderMapper;
        this.jobPostingRepository = jobPostingRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.timeSheetRepository = timeSheetRepository;
    }

    public String autoGenerateWorkOrderId() {
        // Below will cause issues if a Work Order is deleted from the DB.
        long countWorkOrders = this.workOrderRepository.count();
        return String.format("WO-%03d", countWorkOrders + 1);
    }

    public List<WorkOrderCardDto> getAllWorkOrdersAsCards(UUID loggedUserUUid) {
        List<WorkOrder> allWorkOrders = this.workOrderRepository.getAllByJobPosting_BuyerUuid(loggedUserUUid);
        return this.workOrderMapper.mapListOfWorkOrderToListCardDto(allWorkOrders);
    }

    public void createWorOrder(WorkOrderCreateDto dto) {
        JobPosting jobPosting = this.jobPostingRepository.getJobPostingByJobPostingId(dto.getJobPostingId());
        JobSeeker jobSeeker = this.jobSeekerRepository.getJobSeekerByJobSeekerId(dto.getJobSeekerId());
        Supplier supplier = jobSeeker.getSupplier();
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(this.autoGenerateWorkOrderId());
        workOrder.setJobPosting(jobPosting);
        workOrder.setJobSeeker(jobSeeker);
        workOrder.setStartDate(jobPosting.getStartDate());
        workOrder.setEndDate(jobPosting.getEndDate());
        // the Worker will be added to the Work Order during the registration of the Worker.
        workOrder.setNotes(dto.getNotes());
        workOrder.setPayRate(dto.getPayRate());
        workOrder.setSupplier(supplier);
        workOrder.setStatus(WorkOrderStatus.ACTIVE);
        this.workOrderRepository.save(workOrder);


    }

    private String generateWorkOrderId() {
        // this will cause issues if the User can delete record from the database.
        // but they are not supposed to delete from the db. The DB should keep the records forever
        long workOrderCount = this.workOrderRepository.count();
        return String.format("WO-%03d", workOrderCount + 1);
    }

    public int countBuyerWorkOrders(Buyer buyer) {
        // Count the buyer work orders
        return this.workOrderRepository.countByJobPosting_Buyer(buyer);
    }

    public WorkOrderDetailDto getWorkOrderDetails(String workOrderId) {
        WorkOrder workOrder = this.workOrderRepository.findWorkOrderByWorkOrderId(workOrderId)
                .orElseThrow(() -> new RuntimeException("Work Order not found with ID: " + workOrderId));

        return this.workOrderMapper.mapWorkOrderToWorkOrderDetailDto(workOrder);
    }

    public WorkOrderDetailDto getWorkOrderForRegisteredWorker(String workOrderId) {
        WorkOrder workOrder = this.workOrderRepository.findWorkOrderByWorkOrderId(workOrderId)
                .orElseThrow(() -> new RuntimeException("Work Order not found with ID: " + workOrderId));

        WorkOrderDetailDto dto = this.workOrderMapper.mapWorkOrderToWorkOrderDetailDto(workOrder);
        dto.setAssignedWorker(workOrder.getWorker());
        return dto;
    }


    public WorkOrder getWorkOrderById(String workOrderId) {
        Optional<WorkOrder> optionalWorkOrder = this.workOrderRepository.findWorkOrderByWorkOrderId(workOrderId);
        return optionalWorkOrder.orElseThrow(() -> new RuntimeException("Invalid Work Order id"));
    }

    public long countWorkerWorkOrders(Worker worker) {
        return this.workOrderRepository.countWorkOrderByWorker(worker);
    }

    public long countBuyerWorkers(UUID buyerUuid) {
        return this.workOrderRepository.countWorkersForBuyer(buyerUuid);
    }

    public long countWorkerPendingTimeSheets(Worker worker) {
        return this.timeSheetRepository.countAllPendingWorkerTimeSheets(worker);
    }

    public void closeWorkOrderByWorkOrderId(String workOrderId) {
        WorkOrder workOrderToClose = this.workOrderRepository.getWorkOrderByWorkOrderId(workOrderId);
        workOrderToClose.setStatus(WorkOrderStatus.CLOSED);
        this.workOrderRepository.save(workOrderToClose);
    }


}
