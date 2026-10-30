package springbootapp.minivms.mappers;

import org.springframework.stereotype.Component;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderCardDto;
import springbootapp.minivms.model.dto.workitemdto.workorder.WorkOrderDetailDto;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.List;

@Component
public class WorkOrderMapper {

    public List<WorkOrderCardDto> mapListOfWorkOrderToListCardDto(List<WorkOrder> workOrders) {
        return workOrders.stream().map(this::mapWorkOrderToWorkOrderCardDto).toList();
    }

    public WorkOrderCardDto mapWorkOrderToWorkOrderCardDto(WorkOrder workOrder) {
        WorkOrderCardDto workOrderCardDto = new WorkOrderCardDto();
        workOrderCardDto.setId(workOrder.getWorkOrderId());
        workOrderCardDto.setStatus(workOrder.getStatus());
        workOrderCardDto.setAssignedWorkerName(
                workOrder.getJobSeeker().getFirstName()
                + " " +
                workOrder.getJobSeeker().getLastName());
        workOrderCardDto.setStartDate(workOrder.getStartDate());
        workOrderCardDto.setEndDate(workOrder.getEndDate());
        return workOrderCardDto;
    }

    public WorkOrderDetailDto mapWorkOrderToWorkOrderDetailDto(WorkOrder workOrder) {
        WorkOrderDetailDto dto = new WorkOrderDetailDto();
        dto.setId(workOrder.getWorkOrderId());
        String assignedWorkerName = workOrder.getJobSeeker().getFirstName() + " " + workOrder.getJobSeeker().getLastName();
        dto.setAssignedWorkerName(assignedWorkerName);
        dto.setNotes(workOrder.getNotes());
        dto.setStatus(workOrder.getStatus());
        dto.setStartDate(workOrder.getStartDate());
        dto.setEndDate(workOrder.getEndDate());
        dto.setPayRate(workOrder.getPayRate());
        return dto;
    }

}
