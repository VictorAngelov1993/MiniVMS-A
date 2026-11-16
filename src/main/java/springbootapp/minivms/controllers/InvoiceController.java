package springbootapp.minivms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoiceCardDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoicePreviewDTO;
import springbootapp.minivms.services.personservices.SupplierService;
import springbootapp.minivms.services.workitemsservices.InvoiceService;

import java.util.List;
import java.util.UUID;

@Controller
public class InvoiceController {

    private InvoiceService invoiceService;
    private SupplierService supplierService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService,
                             SupplierService supplierService) {
        this.invoiceService = invoiceService;
        this.supplierService = supplierService;
    }

    @GetMapping("buyer/invoices")
    public String loadInvoiceListForBuyer(@AuthenticationPrincipal LoggedUserDto loggedUserDto,
                                          Model model) {

        List<InvoiceCardDto> invoiceCardDtoList = this.invoiceService.getAllInvoicesForTheBuyerReturnedAsCard(loggedUserDto.getUuid());
        model.addAttribute("invoices", invoiceCardDtoList);

        return "buyer/invoices";
    }

    @GetMapping("buyer/invoices/create")
    public String loadTheCreateInvoicePage(
            @RequestParam(required = false) String invoiceId,
            @RequestParam(required = false) UUID supplierId,
            Model model) {

        // 1. If invoiceId came back in the request (during preview reload), keep it.
        // Otherwise, generate a fresh unique one for a brand new invoice.
        String currentInvoiceId = (invoiceId != null && !invoiceId.isBlank())
                ? invoiceId
                : invoiceService.generateInvoiceId();

        // 2. Pass all attributes expected by your HTML template
        model.addAttribute("invoiceId", currentInvoiceId);
        model.addAttribute("suppliers", supplierService.findAllSuppliersForDropDown());
        model.addAttribute("selectedSupplierId", supplierId);

        // 3. If the user clicked "Generate Invoice" (so a supplier is selected), build the preview
        if (supplierId != null) {
            InvoicePreviewDTO preview = invoiceService.buildPreview(currentInvoiceId, supplierId);
            model.addAttribute("invoicePreview", preview);
        }

        return "buyer/create-invoice";
    }

}
