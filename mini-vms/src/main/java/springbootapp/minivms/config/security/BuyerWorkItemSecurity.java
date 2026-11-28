package springbootapp.minivms.config.security;

import org.springframework.stereotype.Component;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;
import springbootapp.minivms.repositories.workitems.InvoiceRepository;
import springbootapp.minivms.repositories.workitems.JobPostingRepository;
import springbootapp.minivms.repositories.workitems.TimeSheetRepository;
import springbootapp.minivms.repositories.workitems.WorkOrderRepository;

import java.util.UUID;

@Component
public class BuyerWorkItemSecurity {

    private final JobPostingRepository jobPostingRepository;
    private final WorkOrderRepository workOrderRepository;
    private final WorkerRepository workerRepository;
    private final TimeSheetRepository timeSheetRepository;
    private final InvoiceRepository invoiceRepository;

    public BuyerWorkItemSecurity(JobPostingRepository jobPostingRepository,
                                 WorkOrderRepository workOrderRepository,
                                 WorkerRepository workerRepository,
                                 TimeSheetRepository timeSheetRepository,
                                 InvoiceRepository invoiceRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.workOrderRepository = workOrderRepository;
        this.workerRepository = workerRepository;
        this.timeSheetRepository = timeSheetRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public boolean isOwned(String workItemId, UUID buyerUuid) {
        if(workItemId.startsWith("JP")) {
            return this.jobPostingRepository.findByJobPostingId(workItemId)
                    .map(jp -> jp.getBuyer().getUuid().equals(buyerUuid))
                    .orElse(false);
        } else if (workItemId.startsWith("WO")) {
            return this.workOrderRepository.findWorkOrderByWorkOrderId(workItemId)
                    .map(wo -> wo.getJobPosting().getBuyer().getUuid().equals(buyerUuid))
                    .orElse(false);
        } else if (workItemId.startsWith("WK")) {
            return this.workerRepository.getWorkerByWorkerId(workItemId)
                    .map(wk -> wk.getWorkOrders().getFirst().getJobPosting().getBuyer().getUuid().equals(buyerUuid))
                    .orElse(false);
        } else if (workItemId.startsWith("TS")) {
            return this.timeSheetRepository.findByTimeSheetId(workItemId)
                    .map(ts -> ts.getWorkOrder().getJobPosting().getBuyer().getUuid().equals(buyerUuid))
                    .orElse(false);
        } else if (workItemId.startsWith("INV")) {
            return this.invoiceRepository.findByInvoiceId(workItemId)
                    .map(inv -> inv.getBuyer().getUuid().equals(buyerUuid))
                    .orElse(false);
        } else {
            return false;
        }

    }

}
