package springbootapp.minivms.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String displayTimeSheetsForTheWorker(@AuthenticationPrincipal LoggedUserDto loggedUser, Model model) {

        List<TimeSheetRowDto> workerTimeSheets = this.timeSheetService.getWorkerTimeSheetsRowDto(loggedUser.getUuid());
        model.addAttribute("timesheets", workerTimeSheets);

        return "worker/timesheets";
    }

    @PreAuthorize("@timeSheetSecurity.isOwned(#id, authentication.name)")
    @GetMapping("/worker/timesheets/{id}/edit")
    public String displayTimeSheetEditPage(@PathVariable String id,
                                           Model model) {
        TimeSheetEditDto timeSheet = this.timeSheetService.getTimeSheetEditDtoById(id);
        TimeSheetSubmitDto submitDto = new TimeSheetSubmitDto();
        submitDto.setTimeSheetId(timeSheet.getId());
        submitDto.setDays(timeSheet.getDays());
        submitDto.setNotes(timeSheet.getNotes());

        model.addAttribute("timesheet", timeSheet);
        model.addAttribute("submitDto", submitDto);

        return "worker/timesheet-edit";
    }

    @PreAuthorize("@timeSheetSecurity.isOwned(#id, authentication.name)")
    @PostMapping("/worker/timesheets/{id}/submit")
    public String submitTimeSheet(@PathVariable("id") String id,
                                  @Valid @ModelAttribute("submitDto")TimeSheetSubmitDto submitDto,
                                  BindingResult bindingResult, Model model) {

        if (submitDto.getTimeSheetId() == null) {
            submitDto.setTimeSheetId(id);
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("timesheet", this.timeSheetService.getTimeSheetEditDtoById(id));
            return "worker/timesheet-edit";
        }

        this.timeSheetService.submitTimeSheet(submitDto);

        return "redirect:/worker/timesheets";
    }

    @GetMapping("buyer/timesheets")
    public String displayTimeSheetsForBuyer(@AuthenticationPrincipal LoggedUserDto loggedUser, Model model) {


        List<TimeSheetRowDto> buyerPendingApprovalTimeSheets = this.timeSheetService.getAllTimeSheetsRowPendingApprovalForTheBuyer(loggedUser.getUuid());
        model.addAttribute("timesheets", buyerPendingApprovalTimeSheets);

        return "buyer/timesheets";
    }

    @GetMapping("buyer/timesheets/history")
    public String displayApprovedAndInvoicedTimeSheetsForBuyer(@AuthenticationPrincipal LoggedUserDto loggedUserDto, Model model) {

        List<TimeSheetRowDto> buyerApprovedAndInvoicedTimeSheets = this.timeSheetService.getApprovedAndInvoicedTimeSheetsRow(loggedUserDto.getUuid());
        model.addAttribute("timesheets", buyerApprovedAndInvoicedTimeSheets);

        return "buyer/timesheets-approved-invoiced";

    }
    @PreAuthorize("@buyerWorkItemSecurity.isOwned(#id, authentication.principal.uuid)")
    @GetMapping("/buyer/timesheets/{id}/edit")
    public String displayTimeSheetEditPageForBuyer(@PathVariable String id,
                                                   Model model) {
        TimeSheetEditBuyerViewDto timeSheet = this.timeSheetService.getTimeSheetEditBuyerViewDto(id);
        model.addAttribute("timesheet", timeSheet);

        return "buyer/timesheet-edit";
    }
    @PreAuthorize("@buyerWorkItemSecurity.isOwned(#id, authentication.principal.uuid)")
    @PostMapping("buyer/timesheets/{id}/approve")
    public String buyerApproveTimeSheet(@PathVariable String id) {
        this.timeSheetService.approveTimeSheet(id);

        return "redirect:/buyer/timesheets";
    }
    @PreAuthorize("@buyerWorkItemSecurity.isOwned(#id, authentication.principal.uuid)")
    @PostMapping("buyer/timesheets/{id}/reject")
    public String buyerRejectTimeSheet(@PathVariable String id) {
        this.timeSheetService.rejectTimeSheet(id);

        return "redirect:/buyer/timesheets";
    }

}
