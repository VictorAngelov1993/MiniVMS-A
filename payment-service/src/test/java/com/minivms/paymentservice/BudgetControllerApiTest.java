package com.minivms.paymentservice;

import com.minivms.paymentservice.model.dto.BudgetRequestDto;
import com.minivms.paymentservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BudgetControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Test
    void addBudgetEndpointReturnsOkAndTotalBudget() throws Exception {
        String token = this.jwtService.generateToken("minivms-client", List.of("ROLE_ADMIN"));

        BudgetRequestDto request = new BudgetRequestDto();
        request.setBuyerUuid(UUID.randomUUID());
        request.setAmountToAdd(new BigDecimal("150.00"));

        this.mockMvc.perform(post("/api/budgets/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBudget").value(150.00));
    }
}