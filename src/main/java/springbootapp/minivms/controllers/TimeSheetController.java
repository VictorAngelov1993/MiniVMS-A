package springbootapp.minivms.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetEditBuyerViewDto;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetEditDto;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetRowDto;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetSubmitDto;
import springbootapp.minivms.services.workitemsservices.TimeSheetService;

import java.util.List;

@Controller
public class TimeSheetController {

    private final TimeSheetService timeSheetService;

    @Autowired
    public TimeSheetController(TimeSheetService timeSheetService) {
        this.timeSheetService = timeSheetService;
    }

    @GetMapping("worker/timesheets")
    public String displayTimeSheetsForTheWorker(HttpSession session, Model model) {

        LoggedUserDto loggedUser = (LoggedUserDto) session.getAttribute("loggedUser");

        // get all worker time sheets from all work orders and display them.
        List<TimeSheetRowDto> workerTimeSheets = this.timeSheetService.getWorkerTimeSheetsRowDto(loggedUser.getUuid());
        model.addAttribute("timesheets", workerTimeSheets);

        return "worker/timesheets";
    }

    @GetMapping("/worker/timesheets/{id}/edit")
    public String displayTimeSheetEditPage(@PathVariable String id,
                                           Model model) {
        TimeSheetEditDto timeSheet = this.timeSheetService.getTimeSheetEditDtoById(id);
        model.addAttribute("timesheet", timeSheet);

        return "worker/timesheet-edit";
    }

    @PostMapping("/worker/timesheets/{id}/submit")
    public String submitTimeSheet(@PathVariable("id") String id,
                                  @ModelAttribute("timesheet")TimeSheetSubmitDto submitDto) {

        if (submitDto.getTimeSheetId() == null) {
            submitDto.setTimeSheetId(id);
        }

        this.timeSheetService.submitTimeSheet(submitDto);

        return "redirect:/worker/timesheets";
    }

    @GetMapping("buyer/timesheets")
    public String displayTimeSheetsForBuyer(HttpSession session, Model model) {
        LoggedUserDto loggedUserDto = (LoggedUserDto) session.getAttribute("loggedUser");

        List<TimeSheetRowDto> buyerPendingApprovalTimeSheets = this.timeSheetService.getAllTimeSheetsRowPendingApprovalForTheBuyer(loggedUserDto.getUuid());

        model.addAttribute("timesheets", buyerPendingApprovalTimeSheets);

        return "buyer/timesheets";
    }

    @GetMapping("/buyer/timesheets/{id}/edit")
    public String displayTimeSheetEditPageForBuyer(@PathVariable String id,
                                                   Model model) {
        TimeSheetEditBuyerViewDto timeSheet = this.timeSheetService.getTimeSheetEditBuyerViewDto(id);
        model.addAttribute("timesheet", timeSheet);

        return "buyer/timesheet-edit";
    }

    @PostMapping("buyer/timesheets/{id}/approve")
    public String buyerApproveTimeSheet(@PathVariable String id,
                                        Model model) {
        // Approve the TimeSheet
        this.timeSheetService.approveTimeSheet(id);

        return "redirect:/buyer/timesheets";
    }

    @PostMapping("buyer/timesheets/{id}/reject")
    public String buyerRejectTimeSheet(@PathVariable String id,
                                        Model model) {
        // Reject the time sheet
        //TODO the UI has section for Reject Reason. I will need to implement that in the future.
        // because I do not have a field for Reject reason in the Time Sheet entity
        this.timeSheetService.rejectTimeSheet(id);

        return "redirect:/buyer/timesheets";
    }

}
