package springbootapp.minivms.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.services.personservices.BuyerService;
import springbootapp.minivms.services.workitemsservices.TimeSheetService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ApprovedTimeSheetReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(ApprovedTimeSheetReminderScheduler.class);
    private final BuyerService buyerService;
    private final TimeSheetService timeSheetService;
    private final Map<UUID, Long> uninvoicedApprovedCounts = new ConcurrentHashMap<>();

    public ApprovedTimeSheetReminderScheduler(BuyerService buyerService, TimeSheetService timeSheetService) {
        this.buyerService = buyerService;
        this.timeSheetService = timeSheetService;
    }

    @Scheduled(initialDelay = 0L, fixedRate = 3_600_000L)
    public void checkApprovedTimesheetsForAllBuyers() {
        List<Buyer> buyers = this.buyerService.getAllBuyers();

        for(Buyer buyer : buyers) {
            Long count = this.timeSheetService.countApprovedTimeSheetsForBuyer(buyer);
            uninvoicedApprovedCounts.put(buyer.getUuid(), count);
        }
        log.info("Hourly check complete: evaluated approved timesheets for {} buyer(s)", buyers.size());
    }

    public Long getUninvoicedApprovedCountForUser(UUID buyerUuid) {
        return uninvoicedApprovedCounts.getOrDefault(buyerUuid, 0L);
    }




}
