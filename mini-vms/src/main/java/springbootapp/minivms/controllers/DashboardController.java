package springbootapp.minivms.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springbootapp.minivms.model.dto.persondto.BuyerDashboardStatsDto;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.persondto.SupplierDashboardDto;
import springbootapp.minivms.model.dto.persondto.WorkerDashboardStatsDto;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.schedulers.ApprovedTimeSheetReminderScheduler;
import springbootapp.minivms.services.personservices.BuyerService;
import springbootapp.minivms.services.personservices.SupplierService;
import springbootapp.minivms.services.personservices.WorkerService;
import springbootapp.minivms.services.workitemsservices.JobPostingService;
import springbootapp.minivms.services.workitemsservices.JobSeekerService;
import springbootapp.minivms.services.workitemsservices.TimeSheetService;
import springbootapp.minivms.services.workitemsservices.WorkOrderService;

import java.security.Principal;
import java.util.Optional;

@Controller
public class DashboardController {

    private final JobPostingService jobPostingService;
    private final BuyerService buyerService;
    private final SupplierService supplierService;
    private final JobSeekerService jobSeekerService;
    private final WorkOrderService workOrderService;
    private final WorkerService workerService;
    private final TimeSheetService timeSheetService;
    private final ApprovedTimeSheetReminderScheduler approvedTimeSheetReminderScheduler;

    @Autowired
    public DashboardController(JobPostingService jobPostingService,
                               BuyerService buyerService,
                               SupplierService supplierService,
                               JobSeekerService jobSeekerService,
                               WorkOrderService workOrderService,
                               WorkerService workerService,
                               TimeSheetService timeSheetService,
                               ApprovedTimeSheetReminderScheduler approvedTimeSheetReminderScheduler) {
        this.jobPostingService = jobPostingService;
        this.buyerService = buyerService;
        this.supplierService = supplierService;
        this.jobSeekerService = jobSeekerService;
        this.workOrderService = workOrderService;
        this.workerService = workerService;
        this.timeSheetService = timeSheetService;
        this.approvedTimeSheetReminderScheduler = approvedTimeSheetReminderScheduler;
    }


    @GetMapping("/buyer/dashboard")
    public String goToBuyerDashboard(Model model, Principal principal) {
        String username = principal.getName();

        BuyerDashboardStatsDto stats = new BuyerDashboardStatsDto();

        Optional<Buyer> getBuyer = this.buyerService.getBuyerByUsername(username);
        Buyer buyer;
        try {
            buyer = getBuyer.orElseThrow(() -> new IllegalStateException("Something went wrong. Contact Support"));
        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "redirect:/access-denied";
        }

        int countJobPostings = this.jobPostingService.countJobPostingsForBuyer(buyer);
        stats.setJobPostingCount(countJobPostings);

        int countWorkOrders = this.workOrderService.countBuyerWorkOrders(buyer);
        stats.setWorkOrderCount(countWorkOrders);

        long countWorkers = this.workOrderService.countBuyerWorkers(buyer);
        stats.setActiveWorkerCount(countWorkers);

        long countPendingTimeSheets = this.timeSheetService.countPendingTimeSheetForBuyerByBuyerUuid(buyer);
        stats.setPendingTimesheetCount(countPendingTimeSheets);

        Long approvedTimeSheetToInvoice = this.approvedTimeSheetReminderScheduler.getUninvoicedApprovedCountForUser(buyer.getUuid());
        model.addAttribute("approvedTimesheetsToInvoice", approvedTimeSheetToInvoice);

        model.addAttribute("stats", stats);


        return "buyer/dashboard";
    }

    @GetMapping("supplier/dashboard")
    public String goToSupplierDashboard(Model model, @AuthenticationPrincipal LoggedUserDto user, Principal principal) {

        Optional<Supplier> loggedSupplier = this.supplierService.getSupplierByUuid(user.getUuid());

        Supplier supplier;

        try{
            supplier = loggedSupplier.orElseThrow(() -> new IllegalStateException("Something went wrong. Contact Support"));
        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "redirect:/access-denied";
        }
        SupplierDashboardDto supplierDashboardStats = new SupplierDashboardDto();
        long countSupplierJobSeekers = this.jobSeekerService.countTheSupplierJobSeekers(supplier);
        supplierDashboardStats.setJobSeekerCount(countSupplierJobSeekers);
        long countSubmittedJobSeekers = this.supplierService.countSubmittedJobSeekers(supplier);
        long countActiveJobSeekers = this.supplierService.countActiveJobSeekers(supplier);
        supplierDashboardStats.setActiveAssignmentCount(countActiveJobSeekers);
        supplierDashboardStats.setSubmittedCandidateCount(countSubmittedJobSeekers);
        model.addAttribute("stats", supplierDashboardStats);

        return "supplier/dashboard";
    }

    @GetMapping("worker/dashboard")
    public String goToWorkerDashboard(Model model, @AuthenticationPrincipal LoggedUserDto loggedUser) {

        Optional<Worker> optionalWorker = this.workerService.getWorkerByUuid(loggedUser.getUuid());
        Worker worker;

        try{
            worker = optionalWorker.orElseThrow(() -> new IllegalStateException("Something went wrong. Contact Support"));
        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "redirect:/access-denied";
        }
        long countWorkerWorkOrders = this.workOrderService.countWorkerWorkOrders(worker);
        WorkerDashboardStatsDto workerDashboardStatsDto = new WorkerDashboardStatsDto();
        workerDashboardStatsDto.setAssignedWorkOrderCount(countWorkerWorkOrders);
        long countWorkerPendingTimeSheets = this.workOrderService.countWorkerPendingTimeSheets(worker);
        workerDashboardStatsDto.setPendingTimesheetCount(countWorkerPendingTimeSheets);
        model.addAttribute("stats", workerDashboardStatsDto);

        return "worker/dashboard";
    }


}
