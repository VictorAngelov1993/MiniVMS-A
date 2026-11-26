package springbootapp.minivms.services.personservices;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import springbootapp.minivms.exception.custom.NotExistingWorker;
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
    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);

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
        if(this.workerRepository.countAllByUsername(worker.getUsername()) == 1
        || this.buyerRepository.countAllByUsername(worker.getUsername()) == 1
        || this.supplierRepository.countAllByUsername(worker.getUsername()) == 1) {
            log.warn("Username {} already exists", worker.getUsername());
            throw new IllegalArgumentException("Username already exist");
        }
        worker.setWorkerId(this.generateWorkerID());
        this.workerRepository.save(worker);

    }

    private String generateWorkerID() {
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
        Worker worker = this.getWorkerByWorkerId(workerId).orElseThrow(() -> new NotExistingWorker("Invalid Worker Id"));
        return this.modelMapper.map(worker, WorkerDetailDto.class);

    }
}
