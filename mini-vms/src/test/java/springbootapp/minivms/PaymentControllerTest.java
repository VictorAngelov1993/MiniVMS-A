package springbootapp.minivms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springbootapp.minivms.controllers.PaymentController;
import springbootapp.minivms.model.dto.paymentdto.BudgetResponseDto;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.entities.enums.Role;
import springbootapp.minivms.services.personservices.BuyerService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PaymentController.class)
@Import(PaymentControllerTest.PrincipalResolverConfig.class)
public class PaymentControllerTest {

    @TestConfiguration
    static class PrincipalResolverConfig implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new AuthenticationPrincipalArgumentResolver());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuyerService buyerService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLoadBudget() throws Exception {
        UUID userUuid = UUID.randomUUID();

        LoggedUserDto loggedUser = new LoggedUserDto();
        loggedUser.setUuid(userUuid);
        loggedUser.setRole(Role.BUYER);
        loggedUser.setUsername("BuyerTest");

        BudgetResponseDto responseDto = new BudgetResponseDto();
        responseDto.setTotalBudget(new BigDecimal("1500.00"));

        when(buyerService.viewBudget(userUuid)).thenReturn(responseDto);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(loggedUser, null, loggedUser.getAuthorities())
        );
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/budgets")
                        .with(securityContext(securityContext)))
                .andDo(result -> {
                    System.out.println("Exception: " + result.getResolvedException());
                    System.out.println("View: " + result.getModelAndView().getViewName());
                    System.out.println("Model: " + result.getModelAndView().getModel());
                })
                .andExpect(status().isOk())
                .andExpect(view().name("buyer/display-budget"))
                .andExpect(model().attribute("budgetAmount", new BigDecimal("1500.00")));

        verify(buyerService).viewBudget(userUuid);
    }
}