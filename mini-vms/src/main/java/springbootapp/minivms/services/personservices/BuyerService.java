package springbootapp.minivms.services.personservices;



import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import springbootapp.minivms.exception.custom.BudgetNotFoundException;
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
    private static final Logger log = LoggerFactory.getLogger(BuyerService.class);


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
    @CacheEvict(value = "buyersList", allEntries = true)
    public void register(Buyer buyer) {
        //Below checks if a different user role has the same Username
        if(this.buyerRepository.countAllByUsername(buyer.getUsername()) == 1
        || this.supplierRepository.countAllByUsername(buyer.getUsername()) == 1
        || this.workerRepository.countAllByUsername(buyer.getUsername()) == 1) {
            log.warn("Buyer registration rejected: username '{}' already exists", buyer.getUsername());
            throw new IllegalArgumentException("Username already exist");
        }
        this.buyerRepository.save(buyer);
        log.info("Registered new buyer with username='{}'", buyer.getUsername());

    }
    @Cacheable(value = "buyersList", key = "'username:' + #username")
    public Optional<Buyer> getBuyerByUsername(String username) {
        return this.buyerRepository.getBuyerByUsername(username);
    }
    @Cacheable(value = "buyersList", key = "'uuid:' + #uuid")
    public Buyer getBuyerByUuid(UUID uuid) {
        return this.buyerRepository.getBuyerByUuid(uuid).orElseThrow(() -> new UserNotFoundException("Buyer Not Found"));
    }

    public BudgetResponseDto addBudget(UUID buyerUuid, BigDecimal amountToAdd) {
        BudgetRequestDto requestDto = new BudgetRequestDto();
        requestDto.setBuyerUuid(buyerUuid);
        requestDto.setAmountToAdd(amountToAdd);
        log.info("Sending Buyer UUID {} and budget amount {} to the payment service", buyerUuid, amountToAdd);
        BudgetResponseDto response = paymentFeignClient.addBudget(requestDto);
        log.info("Successfully added {} to buyer with UUID {}", amountToAdd, buyerUuid);
        return response;
    }

    public BudgetResponseDto viewBudget(UUID buyerUuid) {
        log.info("Requesting budget details for buyerUuid={}", buyerUuid);
        try {
            BudgetResponseDto responseDto = this.paymentFeignClient.viewBudget(buyerUuid);
            log.info("Retrieved budget details for buyerUuid={}", buyerUuid);
            return responseDto;
        } catch (FeignException.NotFound ex) {
            log.warn("No budget account found in payment service for buyerUuid={}", buyerUuid);
            throw new BudgetNotFoundException("No budget account found.");
        }
    }
    @Cacheable(value = "buyersList", key = "'all'")
    public List<Buyer> getAllBuyers() {
        return this.buyerRepository.findAll();
    }


}
