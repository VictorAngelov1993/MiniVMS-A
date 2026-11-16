package springbootapp.minivms.mappers;

import org.springframework.stereotype.Component;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoiceCardDto;
import springbootapp.minivms.model.entities.workitems.Invoice;

import java.util.List;

@Component
public class InvoiceMapper {

    public List<InvoiceCardDto> mapListOfInvoicesToListOfInvoiceCardDto(List<Invoice> invoices) {
        return invoices.stream().map(this::mapInvoiceToInvoiceCardDto).toList();
    }

    public InvoiceCardDto mapInvoiceToInvoiceCardDto(Invoice invoice) {
        InvoiceCardDto invoiceCardDto = new InvoiceCardDto();
        invoiceCardDto.setInvoiceId(invoice.getInvoiceId());
        invoiceCardDto.setStatus(invoice.getStatus());
        invoiceCardDto.setSupplierName(invoice.getSupplierName());
        invoiceCardDto.setTimeSheetCount(invoice.getTimeSheets().size());
        invoiceCardDto.setTotalAmount(invoice.getTotalAmount());
        return invoiceCardDto;

    }
}
