package springbootapp.minivms.mappers;

import org.springframework.stereotype.Component;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerCardDto;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.List;

@Component
public class WorkerMapper {

    public WorkerCardDto getWorkerCardDtoFromWorker(Worker worker) {
        WorkerCardDto workerCardDto = new WorkerCardDto();
        workerCardDto.setId(worker.getWorkerId());
        workerCardDto.setFullName(worker.getFullName());
        workerCardDto.setContactInfo(worker.getEmail()); // Here I will also need to add the phone number but the relation is hard will implement later
        List<WorkOrder> workerWorkOrders = worker.getWorkOrders();
        workerCardDto.setAssignedWorkOrderCount(workerWorkOrders.size());
        return workerCardDto;
    }

    public List<WorkerCardDto> getWorkerCardsFromListOfWorkers(List<Worker> workers) {
        // if no workers are registered .stream will throw an exception therefore below is to prevent that
        if(workers == null || workers.isEmpty()) {
            return List.of();
        }
        return workers.stream().map(this::getWorkerCardDtoFromWorker).toList();
    }
}
