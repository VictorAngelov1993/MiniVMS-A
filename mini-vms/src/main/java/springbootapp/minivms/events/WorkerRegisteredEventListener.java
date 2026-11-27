package springbootapp.minivms.events;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import springbootapp.minivms.exception.custom.NotExistingWorkOrder;
import springbootapp.minivms.model.entities.workitems.WorkOrder;
import springbootapp.minivms.services.workitemsservices.TimeSheetService;
import springbootapp.minivms.services.workitemsservices.WorkOrderService;

@Component
public class WorkerRegisteredEventListener {
    private static final Logger log = LoggerFactory.getLogger(WorkerRegisteredEventListener.class);
    private final WorkOrderService workOrderService;
    private final TimeSheetService timeSheetService;

    public WorkerRegisteredEventListener(WorkOrderService workOrderService, TimeSheetService timeSheetService) {
        this.workOrderService = workOrderService;
        this.timeSheetService = timeSheetService;
    }

    @EventListener
    @Transactional
    public void onWorkerRegistered(WorkerRegisteredEvent event) {
        log.info("Handling WorkerRegisteredEvent for worker {} and work order {}", event.workerUuid(), event.workOrderUuid());
        WorkOrder workOrder = this.workOrderService.getWorkOrderByUuid(event.workOrderUuid()).orElseThrow(() -> new NotExistingWorkOrder("Work Order not found"));
        this.timeSheetService.generateWorkerTimeSheets(workOrder);
    }


}
