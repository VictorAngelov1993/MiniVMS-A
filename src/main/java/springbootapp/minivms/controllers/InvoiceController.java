package springbootapp.minivms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.CreateInvoiceDto;
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


        String currentInvoiceId = (invoiceId != null && !invoiceId.isBlank())
                ? invoiceId
                : invoiceService.generateInvoiceId();


        model.addAttribute("invoiceId", currentInvoiceId);
        model.addAttribute("suppliers", supplierService.findAllSuppliersForDropDown());
        model.addAttribute("selectedSupplierId", supplierId);

        if (supplierId != null) {
            InvoicePreviewDTO preview = invoiceService.buildPreview(currentInvoiceId, supplierId);
            model.addAttribute("invoicePreview", preview);
        }

        return "buyer/create-invoice";
    }

    @PostMapping("buyer/invoices/save")
    public String saveInvoice(@ModelAttribute CreateInvoiceDto formDto,
                              @AuthenticationPrincipal LoggedUserDto loggedUserDto) {
        this.invoiceService.saveInvoice(formDto, loggedUserDto.getUuid());
        return "redirect:/buyer/invoices";
    }

    @GetMapping("buyer/invoices/{id}")
    public String viewInvoiceDetails(@PathVariable("id") String invoiceId,
                                     Model model) {

        InvoicePreviewDTO invoiceDetails = this.invoiceService.getInvoiceDetails(invoiceId);
        model.addAttribute("invoice", invoiceDetails);

        return "buyer/invoice-details";
    }

}
