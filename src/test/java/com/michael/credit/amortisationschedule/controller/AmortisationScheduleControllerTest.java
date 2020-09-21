package com.michael.credit.amortisationschedule.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michael.credit.amortisationschedule.dto.request.AmortisationRequest;
import com.michael.credit.amortisationschedule.dto.response.AmortisationResponse;
import com.michael.credit.amortisationschedule.dto.response.AmortisationScheduleResponse;
import com.michael.credit.amortisationschedule.exception.FieldErrorResponse;
import com.michael.credit.amortisationschedule.service.AmortisationServiceImpl;
import com.michael.credit.amortisationschedule.utils.Round2Scales;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AmortisationScheduleController.class)
class AmortisationScheduleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AmortisationServiceImpl amortisationService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenValidInput_thenReturns200() throws Exception {

        List<AmortisationScheduleResponse> amortisationScheduleResponseSet = new ArrayList<>();

        amortisationScheduleResponseSet.add(AmortisationScheduleResponse
                .builder()
                .year(1)
                .rate(Round2Scales.rint(new BigDecimal(0.18)))
                .amortisationPortion(Round2Scales.rint(new BigDecimal(1.92)))
                .interestPortion(Round2Scales.rint(new BigDecimal(0.24)))
                .balanceDue(Round2Scales.rint(new BigDecimal(18.08)))
                .build()
        );

        AmortisationResponse expectedResponse = AmortisationResponse
                .builder()
                .balanceDueAfterFixedInterestRate(Round2Scales.rint(new BigDecimal(18.08)))
                .monthlyLoanRate(Round2Scales.rint(new BigDecimal(0.18)))
                .amortisationSchedule(amortisationScheduleResponseSet)
                .build();

        given(amortisationService.calculate(any(AmortisationRequest.class))).willReturn(expectedResponse);

        MvcResult mvcResult = mvc.perform(get("/amortisation?loanAmount=20&borrowingRate=1&startRedemption=10&years=1")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        String res = mvcResult.getResponse().getContentAsString();
        System.out.println("RES "+ res);
        AmortisationResponse result = objectMapper.readValue(res, AmortisationResponse.class);

        assertEquals(200, status);
        assertEquals(expectedResponse, result);
    }

    @Test
    void whenUnValidInput_thenReturns400() throws Exception {

        Map<String, String> errors = new HashMap<>();
        errors.put("loanAmount", "Loan amount must be positive");
        FieldErrorResponse expectedResponse = FieldErrorResponse
                .builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        MvcResult mvcResult = mvc.perform(get("/amortisation?loanAmount=-2000&borrowingRate=2&startRedemption=2&years=12")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        FieldErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FieldErrorResponse.class);

        assertEquals(400, status);
        assertEquals(response, expectedResponse);
    }
}