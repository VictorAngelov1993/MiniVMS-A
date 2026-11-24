package springbootapp.minivms.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springbootapp.minivms.model.dto.paymentdto.BudgetResponseDto;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.services.personservices.BuyerService;

import java.math.BigDecimal;

@Controller
public class PaymentController {

    private final BuyerService buyerService;
    @Autowired
    public PaymentController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping("/budgets/add")
    public String loadAddBudgetPage() {
        return "buyer/add-budget";
    }

    @PostMapping("/budgets/add")
    public String addBudget(@AuthenticationPrincipal LoggedUserDto loggedUserDto,
                            @RequestParam("amountToAdd")BigDecimal amountToAdd,
                            RedirectAttributes redirectAttributes) {

        BudgetResponseDto responseDto = this.buyerService.addBudget(loggedUserDto.getUuid(), amountToAdd);
        redirectAttributes.addFlashAttribute("successMessage", responseDto.getResponse());

        return "redirect:/profile";

    }

    @GetMapping("/budgets")
    public String loadBudget(@AuthenticationPrincipal LoggedUserDto loggedUserDto, Model model) {
        BudgetResponseDto responseDto = this.buyerService.viewBudget(loggedUserDto.getUuid());
        model.addAttribute("budgetAmount", responseDto.getTotalBudget());
        return "buyer/display-budget";
    }

}
