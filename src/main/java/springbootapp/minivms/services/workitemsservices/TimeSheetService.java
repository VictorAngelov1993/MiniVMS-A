package springbootapp.minivms.services.workitemsservices;

import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.TimeSheetMapper;
import springbootapp.minivms.model.dto.workitemdto.timesheet.*;
import springbootapp.minivms.model.entities.enums.TimeSheetStatus;
import springbootapp.minivms.model.entities.persons.Worker;
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

        // Below is the method that will generate the Worker Time Sheets on demand.
        // In the future I will test the performance and check if adding time sheets is better to be in the registration method.
        for(WorkOrder wo : workerWorkOrders) {
            this.generateWorkerTimeSheets(wo);
        }


        List<TimeSheet> workerTimeSheets = worker.getWorkOrders().stream()
                .flatMap(wo -> wo.getTimeSheets().stream())
                .sorted(Comparator.comparing(TimeSheet::getWeekStartDay))
                .collect(Collectors.toList());

        return workerTimeSheets;
    }

    public List<TimeSheetRowDto> getWorkerTimeSheetsRowDto(UUID workerUuid) {
        return this.timeSheetMapper.mapListOfTimeSheetsToListOfTimeSheetRowDto(this.getWorkerTimeSheets(workerUuid));
    }

    private void generateWorkerTimeSheets(WorkOrder workerWorkOrder) {
        LocalDate start = workerWorkOrder.getStartDate();
        LocalDate end = workerWorkOrder.getEndDate();
        // below will get Monday of the starting week.
        LocalDate weekStartDay = start.with(DayOfWeek.MONDAY);
        // the below generates all of the worker time sheets and place them in draft status.
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

                    // Below two if statements prevent the worker to add hours on days outside his work order dates
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
        // we get the timesheet from the db.
        TimeSheet timeSheet = this.timeSheetRepository.getTimeSheetByTimeSheetId(timeSheetSubmitDto.getTimeSheetId());
        // we set notes and status
        timeSheet.setNotes(timeSheetSubmitDto.getNotes());
        timeSheet.setStatus(TimeSheetStatus.SUBMITTED);
        // now we get the list of entity TimeSheetDay
        List<TimeSheetDay> timeSheetDays = timeSheet.getDays();
        // we also get the data from the DTO this is coming from the front end. Whatever the user added in the ui
        List<TimeSheetDayDto> timeSheetDayDtos = timeSheetSubmitDto.getDays();

        for(int a = 0; a < timeSheetDays.size(); a++) {
            //below we get each time sheet day from the list and we add the value from the dto
            TimeSheetDay timeSheetDay = timeSheetDays.get(a);
            TimeSheetDayDto timeSheetDayDto = timeSheetDayDtos.get(a);
            //Below is additional secure measure to prevent the worker to add time in days outside his work order dates.
            if(!timeSheetDay.isEditable()) {
                continue;
            }

            timeSheetDay.setHours(timeSheetDayDto.getHours());
        }
        //then we save the time sheet.
        this.timeSheetRepository.save(timeSheet);

    }

    public List<TimeSheetRowDto> getAllTimeSheetsRowPendingApprovalForTheBuyer(UUID buyerUuid) {
        return this.timeSheetMapper.mapListOfTimeSheetsToListOfTimeSheetRowDto(this.getAllTimeSheetsPendingApprovalForTheBuyer(buyerUuid));
    }

    private List<TimeSheet> getAllTimeSheetsPendingApprovalForTheBuyer(UUID buyerUuid) {
        return this.timeSheetRepository.getAllPendingApprovalTimeSheetsForTheBuyer(buyerUuid);
    }

    public long countPendingTimeSheetForBuyerByBuyerUuid(UUID buyerUuid) {
        return this.timeSheetRepository.countPendingTimeSheetsForBuyer(buyerUuid);
    }

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

}
