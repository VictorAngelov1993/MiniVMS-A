package springbootapp.minivms.services.personservices;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.WorkerMapper;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerCardDto;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerDetailDto;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.JobPosting;
import springbootapp.minivms.model.entities.workitems.WorkOrder;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;
import springbootapp.minivms.repositories.workitems.JobPostingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final JobPostingRepository jobPostingRepository;
    private final WorkerMapper workerMapper;
    private final ModelMapper modelMapper;

    public WorkerService(WorkerRepository workerRepository,
                         BuyerRepository buyerRepository,
                         SupplierRepository supplierRepository,
                         JobPostingRepository jobPostingRepository,
                         WorkerMapper workerMapper,
                         ModelMapper modelMapper) {
        this.workerRepository = workerRepository;
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.workerMapper = workerMapper;
        this.modelMapper = modelMapper;
    }

    public void register(Worker worker) {
        //Below checks if a different user role has the same Username
        if(this.workerRepository.countAllByUsername(worker.getUsername()) == 1
        || this.buyerRepository.countAllByUsername(worker.getUsername()) == 1
        || this.supplierRepository.countAllByUsername(worker.getUsername()) == 1) {
            throw new IllegalArgumentException("Username already exist");
        }
        // The Work Order validation is happening in the Work Order Service. The Worker here will have valid Work Order.
        worker.setWorkerId(this.generateWorkerID());
        this.workerRepository.save(worker);

    }

    private String generateWorkerID() {
        // This will fail if a worker is deleted from the database, but users should not be able to delete entities.
        return String.format("WK-%03d", workerRepository.count() + 1);
    }

    private List<Worker> getAllWorkersForTheBuyer(UUID buyerUuid) {
        List<Worker> buyerWorkers = new ArrayList<>();
        List<JobPosting> getBuyerJobPostings = this.jobPostingRepository.getJobPostingForBuyer(buyerUuid);
        for(JobPosting jp : getBuyerJobPostings) {
            List<WorkOrder> wos = jp.getWorkOrders();
            for(WorkOrder wo : wos) {
                if(wo.getWorker() == null) {
                    continue;
                }
                buyerWorkers.add(wo.getWorker());
            }
        }
        return buyerWorkers;
    }

    public List<WorkerCardDto> getAllWorkerCardDto(UUID buyerUuid) {

        return this.workerMapper.getWorkerCardsFromListOfWorkers(this.getAllWorkersForTheBuyer(buyerUuid));
    }
    public Optional<Worker> getWorkerByUuid (UUID uuid) {
        return this.workerRepository.getWorkerByUuid(uuid);
    }

    private Optional<Worker> getWorkerByWorkerId(String workerId) {
        return this.workerRepository.getWorkerByWorkerId(workerId);
    }

    public WorkerDetailDto getWorkerDetailByWorkerId(String workerId) {
        Worker worker = this.getWorkerByWorkerId(workerId).orElseThrow(() -> new RuntimeException("Invalid Worker Id"));
        WorkerDetailDto workerDetailDto = this.modelMapper.map(worker, WorkerDetailDto.class);

        return workerDetailDto;

    }
}
