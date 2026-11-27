package springbootapp.minivms.services.workitemsservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.TimeSheetMapper;
import springbootapp.minivms.model.dto.workitemdto.timesheet.*;
import springbootapp.minivms.model.entities.enums.TimeSheetStatus;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.Invoice;
import springbootapp.minivms.model.entities.workitems.WorkOrder;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheet;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheetDay;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;
import springbootapp.minivms.repositories.workitems.TimeSheetRepository;
import springbootapp.minivms.repositories.workitems.WorkOrderRepository;


import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TimeSheetService {

    private final TimeSheetRepository timeSheetRepository;
    private final WorkerRepository workerRepository;
    private final TimeSheetMapper timeSheetMapper;
    private final WorkOrderRepository workOrderRepository;
    private static final Logger log = LoggerFactory.getLogger(TimeSheetService.class);


    public TimeSheetService(TimeSheetRepository timeSheetRepository,
                            WorkerRepository workerRepository,
                            TimeSheetMapper timeSheetMapper,
                            WorkOrderRepository workOrderRepository) {
        this.timeSheetRepository = timeSheetRepository;
        this.workerRepository = workerRepository;
        this.timeSheetMapper = timeSheetMapper;
        this.workOrderRepository = workOrderRepository;
    }

    private List<TimeSheet> getWorkerTimeSheets(UUID workerUuid) {
        Worker worker = this.workerRepository.getWorkerByUuid(workerUuid).orElseThrow(() -> new RuntimeException("Invalid Worker id"));
        List<WorkOrder> workerWorkOrders = worker.getWorkOrders();

        for(WorkOrder wo : workerWorkOrders) {
            this.generateWorkerTimeSheets(wo);
        }

        return worker.getWorkOrders().stream()
                .flatMap(wo -> wo.getTimeSheets().stream())
                .sorted(Comparator.comparing(TimeSheet::getWeekStartDay))
                .collect(Collectors.toList());
    }

    public List<TimeSheetRowDto> getWorkerTimeSheetsRowDto(UUID workerUuid) {
        return this.timeSheetMapper.mapListOfTimeSheetsToListOfTimeSheetRowDto(this.getWorkerTimeSheets(workerUuid));
    }

    public void generateWorkerTimeSheets(WorkOrder workerWorkOrder) {
        log.info("Starting the process of Generating Time Sheets for the worker");
        LocalDate start = workerWorkOrder.getStartDate();
        LocalDate end = workerWorkOrder.getEndDate();
        LocalDate weekStartDay = start.with(DayOfWeek.MONDAY);
        while (!weekStartDay.isAfter(end)) {
            LocalDate weekEnd = weekStartDay.plusDays(6);

            LocalDate finalWeekStartDay = weekStartDay;
            boolean doesTimeSheetExist = workerWorkOrder.getTimeSheets().stream()
                    .anyMatch(timeSheet -> timeSheet.getWeekStartDay().equals(finalWeekStartDay));

            if(!doesTimeSheetExist) {
                TimeSheet timeSheet = new TimeSheet();
                timeSheet.setWeekStartDay(weekStartDay);
                timeSheet.setWeekEndDay(weekEnd);
                timeSheet.setStatus(TimeSheetStatus.DRAFT);
                timeSheet.setWorkOrder(workerWorkOrder);
                timeSheet.setTimeSheetId(this.generateTimeSheetId());

                for(int a = 0; a < 7; a++) {
                    LocalDate currentDate = weekStartDay.plusDays(a);

                    TimeSheetDay timeSheetDay = new TimeSheetDay();
                    timeSheetDay.setTimeSheet(timeSheet);
                    timeSheetDay.setDate(weekStartDay.plusDays(a));

                    if(currentDate.isBefore(start)) {
                        timeSheetDay.setHours(BigDecimal.ZERO);
                        timeSheetDay.setEditable(false);
                        timeSheet.getDays().add(timeSheetDay);
                        continue;
                    }

                    if(currentDate.isAfter(end)) {
                        timeSheetDay.setHours(BigDecimal.ZERO);
                        timeSheetDay.setEditable(false);
                        timeSheet.getDays().add(timeSheetDay);
                        continue;
                    }

                    if(a < 5) {
                        timeSheetDay.setHours(BigDecimal.valueOf(8));
                    } else {
                        timeSheetDay.setHours(BigDecimal.ZERO);
                    }
                    timeSheetDay.setEditable(true);
                    timeSheet.getDays().add(timeSheetDay);
                }

                workerWorkOrder.getTimeSheets().add(timeSheet);
                this.workOrderRepository.save(workerWorkOrder);

            }

            weekStartDay = weekStartDay.plusWeeks(1);
        }
        log.info("All Time Sheets were created for Worker {}", workerWorkOrder.getWorker().getFullName());

    }



    private String generateTimeSheetId() {
        long timeSheetCount = this.timeSheetRepository.count();
        return String.format("TS-%05d", timeSheetCount + 1);
    }

    private TimeSheet getTimeSheetById(String timeSheetId) {
        return this.timeSheetRepository.getTimeSheetByTimeSheetId(timeSheetId);
    }

    public TimeSheetEditDto getTimeSheetEditDtoById(String timeSheetId) {
        return this.timeSheetMapper.getEditDtoFromTimeSheet(this.getTimeSheetById(timeSheetId));
    }

    public TimeSheetEditBuyerViewDto getTimeSheetEditBuyerViewDto(String timeSheetId) {
        return this.timeSheetMapper.getEditDtoForBuyerViewFromTimeSheet(this.getTimeSheetById(timeSheetId));
    }

    public void submitTimeSheet(TimeSheetSubmitDto timeSheetSubmitDto) {
        TimeSheet timeSheet = this.timeSheetRepository.getTimeSheetByTimeSheetId(timeSheetSubmitDto.getTimeSheetId());
        timeSheet.setNotes(timeSheetSubmitDto.getNotes());
        timeSheet.setStatus(TimeSheetStatus.SUBMITTED);
        List<TimeSheetDay> timeSheetDays = timeSheet.getDays();
        List<TimeSheetDayDto> timeSheetDayDtos = timeSheetSubmitDto.getDays();

        for(int a = 0; a < timeSheetDays.size(); a++) {
            TimeSheetDay timeSheetDay = timeSheetDays.get(a);
            TimeSheetDayDto timeSheetDayDto = timeSheetDayDtos.get(a);
            if(!timeSheetDay.isEditable()) {
                continue;
            }

            timeSheetDay.setHours(timeSheetDayDto.getHours());
        }
        this.timeSheetRepository.save(timeSheet);
        log.info("Time Sheet with ID {} was successfully submitted", timeSheet.getTimeSheetId());

    }

    public List<TimeSheetRowDto> getAllTimeSheetsRowPendingApprovalForTheBuyer(UUID buyerUuid) {
        return this.timeSheetMapper.mapListOfTimeSheetsToListOfTimeSheetRowDto(this.getAllTimeSheetsPendingApprovalForTheBuyer(buyerUuid));
    }

    private List<TimeSheet> getAllTimeSheetsPendingApprovalForTheBuyer(UUID buyerUuid) {
        return this.timeSheetRepository.getAllPendingApprovalTimeSheetsForTheBuyer(buyerUuid);
    }

    public long countPendingTimeSheetForBuyerByBuyerUuid(Buyer buyer) {
        return this.timeSheetRepository.countPendingTimeSheetsForBuyer(buyer);
    }
    @CacheEvict(value = "approvedTimesheetCounts", allEntries = true)
    public void approveTimeSheet(String timeSheetId) {
        TimeSheet timeSheetToApprove = this.timeSheetRepository.getTimeSheetByTimeSheetId(timeSheetId);
        timeSheetToApprove.setStatus(TimeSheetStatus.APPROVED);
        this.timeSheetRepository.save(timeSheetToApprove);
    }
    public void rejectTimeSheet(String timeSheetId) {
        TimeSheet timeSheetToReject = this.timeSheetRepository.getTimeSheetByTimeSheetId(timeSheetId);
        timeSheetToReject.setStatus(TimeSheetStatus.REJECTED);
        this.timeSheetRepository.save(timeSheetToReject);
    }

    public List<TimeSheetDataForInvoiceDTO> getTimeSheetDataForInvoiceDto(UUID supplierUuid) {
        List<TimeSheet> allTimeSheetsForTheSupplier = this.timeSheetRepository.getAllByWorkOrder_Supplier_UuidAndStatus(supplierUuid, TimeSheetStatus.APPROVED);
        return this.timeSheetMapper.mapListTimeSheetToListTimeSheetDataForInvoiceDto(allTimeSheetsForTheSupplier);
    }
    @CacheEvict(value = "approvedTimesheetCounts", key = "#invoice.buyer.uuid")
    public void saveInvoiceIdToTimeSheet(List<String> timeSheetIds, Invoice invoice) {
        for(String timeSheetId : timeSheetIds) {
            TimeSheet ts = this.timeSheetRepository.getTimeSheetByTimeSheetId(timeSheetId);
            ts.setInvoice(invoice);
            ts.setStatus(TimeSheetStatus.INVOICED);
            this.timeSheetRepository.save(ts);
        }
    }

    public List<TimeSheet> getTimeSheetsByTimeSheetId(List<String> timeSheets) {
        List<TimeSheet> timeSheetsToReturn = this.timeSheetRepository.getByTimeSheetIdIn(timeSheets);

        if(timeSheetsToReturn.size() != timeSheets.size()) {
            throw new IllegalArgumentException("Some Time Sheets were not found");
        }
        return timeSheetsToReturn;
    }

    public List<TimeSheetDataForInvoiceDTO> getTimeSheetDetailsForInvoiceDetails(List<TimeSheet> timeSheets) {
        return this.timeSheetMapper.mapListTimeSheetToListTimeSheetDataForInvoiceDto(timeSheets);
    }

    @Cacheable(value = "approvedTimesheetCounts", key = "#buyer.uuid")
    public Long countApprovedTimeSheetsForBuyer(Buyer buyer) {
        return this.timeSheetRepository.countApprovedTimeSheetByBuyer(buyer);
    }

}
