package springbootapp.minivms.services.workitemsservices;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.exception.custom.BudgetNotFoundException;
import springbootapp.minivms.feign.PaymentFeignClient;
import springbootapp.minivms.mappers.InvoiceMapper;
import springbootapp.minivms.model.dto.paymentdto.PaymentRequestDto;
import springbootapp.minivms.model.dto.paymentdto.PaymentResponseDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.CreateInvoiceDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoiceCardDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoicePreviewDTO;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetDataForInvoiceDTO;
import springbootapp.minivms.model.entities.enums.InvoiceStatus;
import springbootapp.minivms.model.entities.enums.PaymentStatus;
import springbootapp.minivms.model.entities.workitems.Invoice;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheet;
import springbootapp.minivms.repositories.workitems.InvoiceRepository;
import springbootapp.minivms.schedulers.ApprovedTimeSheetReminderScheduler;
import springbootapp.minivms.services.personservices.BuyerService;
import springbootapp.minivms.services.personservices.SupplierService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final SupplierService supplierService;
    private final TimeSheetService timeSheetService;
    private final PaymentFeignClient paymentFeignClient;
    private final BuyerService buyerService;
    private final ApprovedTimeSheetReminderScheduler approvedTimeSheetReminderScheduler;



    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceMapper invoiceMapper,
                          SupplierService supplierService,
                          TimeSheetService timeSheetService,
                          PaymentFeignClient paymentFeignClient,
                          BuyerService buyerService,
                          ApprovedTimeSheetReminderScheduler approvedTimeSheetReminderScheduler) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.supplierService = supplierService;
        this.timeSheetService = timeSheetService;
        this.paymentFeignClient = paymentFeignClient;
        this.buyerService = buyerService;
        this.approvedTimeSheetReminderScheduler = approvedTimeSheetReminderScheduler;
    }

    public List<InvoiceCardDto> getAllInvoicesForTheBuyerReturnedAsCard(UUID loggedUserUUid) {
        return this.invoiceMapper.mapListOfInvoicesToListOfInvoiceCardDto(this.invoiceRepository.getAllByBuyer_Uuid(loggedUserUUid));
    }

    public String generateInvoiceId() {
        long invoiceCount = this.invoiceRepository.count();
        return String.format("INV-%03d", invoiceCount + 1);
    }

    public InvoicePreviewDTO buildPreview(String currentInvoiceId, UUID supplierId) {
        InvoicePreviewDTO preview = new InvoicePreviewDTO();
        preview.setInvoiceId(currentInvoiceId);
        preview.setSupplierName(this.supplierService.getSupplierFullNameForUuid(supplierId));
        // Below will get all the Time Sheets for all workers that are associated with the Supplier
        // because the Invoices are per Supplier, the Buyer pays the supplier for each of the workers that the supplier has provided
        preview.setTimeSheets(this.timeSheetService.getTimeSheetDataForInvoiceDto(supplierId));
        BigDecimal totalAmountOfAllTimeSheets = preview.getTimeSheets().stream().map(TimeSheetDataForInvoiceDTO::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        preview.setTotalAmount(totalAmountOfAllTimeSheets);
        return preview;

    }



    public void processPayment(UUID buyerUuid, String invoiceId) {
        PaymentRequestDto requestDto = new PaymentRequestDto();
        Invoice invoice = this.invoiceRepository.getInvoiceByInvoiceId(invoiceId);
        requestDto.setInvoiceId(invoiceId);
        requestDto.setAmount(invoice.getTotalAmount());
        requestDto.setBuyerUuid(buyerUuid);
        requestDto.setSupplierName(invoice.getSupplierName());
        PaymentResponseDto response;
        try {
             response = this.paymentFeignClient.processPayment(requestDto);
        } catch (FeignException.NotFound ex) {
            throw new BudgetNotFoundException("No Budget please add Budget in your Profile");
        }


        if(response.getStatus() != PaymentStatus.REJECTED) {
            invoice.setStatus(InvoiceStatus.PAID);
            this.invoiceRepository.save(invoice);
        }
        if (response.getStatus() == PaymentStatus.REJECTED) {
            invoice.setStatus(InvoiceStatus.REJECTED);
            this.invoiceRepository.save(invoice);
        }
    }

    public void saveInvoice(CreateInvoiceDto invoiceDto, UUID loggedBuyerUuid) {
        Invoice invoiceToSave = new Invoice();
        invoiceToSave.setInvoiceId(invoiceDto.getInvoiceId());
        invoiceToSave.setBuyer(this.buyerService.getBuyerByUuid(loggedBuyerUuid));
        invoiceToSave.setSupplierName(this.supplierService.getSupplierFullNameForUuid(invoiceDto.getSupplierId()));
        invoiceToSave.setTotalAmount(invoiceDto.getTotalAmount());
        invoiceToSave.setStatus(InvoiceStatus.CREATED);
        invoiceToSave.setTimeSheets(this.getTimeSheetsById(invoiceDto.getTimeSheetIds()));
        this.invoiceRepository.save(invoiceToSave);
        this.timeSheetService.saveInvoiceIdToTimeSheet(invoiceDto.getTimeSheetIds(), invoiceToSave);
        this.approvedTimeSheetReminderScheduler.refreshForBuyerUuid(loggedBuyerUuid);
    }

    private List<TimeSheet> getTimeSheetsById(List<String> timeSheetIds) {
        return this.timeSheetService.getTimeSheetsByTimeSheetId(timeSheetIds);
    }

    public InvoicePreviewDTO getInvoiceDetails(String invoiceId) {
        Invoice invoice = this.invoiceRepository.getInvoiceByInvoiceId(invoiceId);
        InvoicePreviewDTO invoiceDetails = this.invoiceMapper.getInvoiceDetailsAsDtoFromInvoice(invoice);
        List<TimeSheetDataForInvoiceDTO> timeSheetDetails = this.timeSheetService.getTimeSheetDetailsForInvoiceDetails(invoice.getTimeSheets());
        invoiceDetails.setTimeSheets(timeSheetDetails);
         return invoiceDetails;
    }

}
