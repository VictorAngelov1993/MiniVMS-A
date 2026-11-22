package springbootapp.minivms.services.personservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.exception.custom.UserNotFoundException;
import springbootapp.minivms.feign.PaymentFeignClient;
import springbootapp.minivms.model.dto.paymentdto.BudgetRequestDto;
import springbootapp.minivms.model.dto.paymentdto.BudgetResponseDto;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final WorkerRepository workerRepository;
    private final PaymentFeignClient paymentFeignClient;

    @Autowired
    public BuyerService(BuyerRepository buyerRepository,
                        SupplierRepository supplierRepository,
                        WorkerRepository workerRepository,
                        PaymentFeignClient paymentFeignClient) {
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.workerRepository = workerRepository;
        this.paymentFeignClient = paymentFeignClient;
    }

    public void register(Buyer buyer) {
        //Below checks if a different user role has the same Username
        if(this.buyerRepository.countAllByUsername(buyer.getUsername()) == 1
        || this.supplierRepository.countAllByUsername(buyer.getUsername()) == 1
        || this.workerRepository.countAllByUsername(buyer.getUsername()) == 1) {
            throw new IllegalArgumentException("Username already exist");
        }
        this.buyerRepository.save(buyer);

    }
    public Optional<Buyer> getBuyerByUsername(String username) {
        return this.buyerRepository.getBuyerByUsername(username);
    }

    public Buyer getBuyerByUuid(UUID uuid) {
        return this.buyerRepository.getBuyerByUuid(uuid).orElseThrow(() -> new UserNotFoundException("Buyer Not Found"));
    }

    public BudgetResponseDto addBudget(UUID buyerUuid, BigDecimal amountToAdd) {
        BudgetRequestDto requestDto = new BudgetRequestDto();
        requestDto.setBuyerUuid(buyerUuid);
        requestDto.setAmountToAdd(amountToAdd);

        return paymentFeignClient.addBudget(requestDto);
    }

    public BudgetResponseDto viewBudget(UUID buyerUuid) {
        return this.paymentFeignClient.viewBudget(buyerUuid);
    }

    public List<Buyer> getAllBuyers() {
        return this.buyerRepository.findAll();
    }
}
