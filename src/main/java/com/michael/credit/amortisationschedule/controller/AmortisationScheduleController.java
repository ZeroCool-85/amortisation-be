package com.michael.credit.amortisationschedule.controller;

import com.michael.credit.amortisationschedule.dto.request.AmortisationRequest;
import com.michael.credit.amortisationschedule.dto.response.AmortisationResponse;
import com.michael.credit.amortisationschedule.service.AmortisationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "amortisation")
public class AmortisationScheduleController {

    @Autowired
    AmortisationServiceImpl amortisationService;

    @GetMapping()
    ResponseEntity<AmortisationResponse> get(@Valid AmortisationRequest amortisationRequest)  {
        AmortisationResponse amortisationResponse = amortisationService.calculate(amortisationRequest);
        return ResponseEntity.ok(amortisationResponse);
    }
}
