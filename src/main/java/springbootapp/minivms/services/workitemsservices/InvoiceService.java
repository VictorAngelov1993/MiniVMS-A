package springbootapp.minivms.services.workitemsservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.InvoiceMapper;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoiceCardDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoicePreviewDTO;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetDataForInvoiceDTO;
import springbootapp.minivms.repositories.workitems.InvoiceRepository;
import springbootapp.minivms.services.personservices.SupplierService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;
    private SupplierService supplierService;
    private TimeSheetService timeSheetService;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceMapper invoiceMapper,
                          SupplierService supplierService,
                          TimeSheetService timeSheetService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.supplierService = supplierService;
        this.timeSheetService = timeSheetService;
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
}
