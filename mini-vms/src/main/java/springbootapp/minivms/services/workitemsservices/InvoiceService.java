package springbootapp.minivms.services.workitemsservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.feign.PaymentFeignClient;
import springbootapp.minivms.mappers.InvoiceMapper;
import springbootapp.minivms.model.dto.paymentdto.PaymentRequestDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.CreateInvoiceDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoiceCardDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoicePreviewDTO;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetDataForInvoiceDTO;
import springbootapp.minivms.model.entities.enums.InvoiceStatus;
import springbootapp.minivms.model.entities.workitems.Invoice;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheet;
import springbootapp.minivms.repositories.workitems.InvoiceRepository;
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



    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceMapper invoiceMapper,
                          SupplierService supplierService,
                          TimeSheetService timeSheetService,
                          PaymentFeignClient paymentFeignClient,
                          BuyerService buyerService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.supplierService = supplierService;
        this.timeSheetService = timeSheetService;
        this.paymentFeignClient = paymentFeignClient;
        this.buyerService = buyerService;
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



    public void approveInvoice(String invoiceId) {
        PaymentRequestDto requestDto = new PaymentRequestDto();


        // ge tthe invoice from the DB and then add the invoice ID supplier name and amount in the requestDto

        this.paymentFeignClient.createPayment(requestDto);
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
