package springbootapp.minivms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootapp.minivms.model.dto.paymentdto.InvoiceConversionDto;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.CreateInvoiceDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoiceCardDto;
import springbootapp.minivms.model.dto.workitemdto.invoice.InvoicePreviewDTO;
import springbootapp.minivms.services.ExchangeRateService;
import springbootapp.minivms.services.personservices.SupplierService;
import springbootapp.minivms.services.workitemsservices.InvoiceService;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final SupplierService supplierService;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService,
                             SupplierService supplierService,
                             ExchangeRateService exchangeRateService) {
        this.invoiceService = invoiceService;
        this.supplierService = supplierService;
        this.exchangeRateService = exchangeRateService;
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
            @AuthenticationPrincipal LoggedUserDto loggedUserDto,
            @RequestParam(required = false) String invoiceId,
            @RequestParam(required = false) UUID supplierId,
            Model model) {


        String currentInvoiceId = (invoiceId != null && !invoiceId.isBlank())
                ? invoiceId
                : invoiceService.generateInvoiceId();


        model.addAttribute("invoiceId", currentInvoiceId);
        model.addAttribute("suppliers", supplierService.getAllSuppliersThatHaveWorkOrderWithTheBuyer(loggedUserDto.getUuid()));
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

    @PreAuthorize("@buyerWorkItemSecurity.isOwned(#invoiceId, authentication.principal.uuid)")
    @GetMapping("buyer/invoices/{id}")
    public String viewInvoiceDetails(@PathVariable("id") String invoiceId,
                                     @RequestParam(name = "targetCurrency", required = false) String targetCurrency,
                                     Model model) {

        InvoicePreviewDTO invoiceDetails = this.invoiceService.getInvoiceDetails(invoiceId);
        model.addAttribute("invoice", invoiceDetails);

        String invoiceCurrency = "USD";
        String resolvedTarget = (targetCurrency != null && !targetCurrency.isBlank())
                ? targetCurrency.trim().toUpperCase(Locale.ROOT)
                : ("USD".equalsIgnoreCase(invoiceCurrency) ? "EUR" : "USD");

        InvoiceConversionDto conversion = exchangeRateService.convertInvoiceAmount(
                invoiceDetails.getTotalAmount(),
                invoiceCurrency,
                resolvedTarget
        );

        model.addAttribute("invoiceCurrency", invoiceCurrency);
        model.addAttribute("conversion", conversion);

        return "buyer/invoice-details";
    }

    @PreAuthorize("@buyerWorkItemSecurity.isOwned(#invoiceId, authentication.principal.uuid)")
    @PostMapping("buyer/invoices/{id}/pay")
    public String processPayment(@PathVariable("id") String invoiceId,
                                 @AuthenticationPrincipal LoggedUserDto loggerUser) {

        this.invoiceService.processPayment(loggerUser.getUuid(), invoiceId);

        return "redirect:/buyer/invoices";
    }

}
