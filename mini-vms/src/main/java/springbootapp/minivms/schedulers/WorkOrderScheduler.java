package springbootapp.minivms.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springbootapp.minivms.services.workitemsservices.WorkOrderService;

@Component
public class WorkOrderScheduler {

    private final WorkOrderService workOrderService;

    public WorkOrderScheduler(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void closeExpiredWorkOrders() {
        this.workOrderService.closeExpiredWorkOrders();
    }

}
