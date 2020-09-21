package com.michael.credit.amortisationschedule.service;

import com.michael.credit.amortisationschedule.dto.request.AmortisationRequest;
import com.michael.credit.amortisationschedule.dto.response.AmortisationResponse;

interface AmortisationService {
    AmortisationResponse calculate(AmortisationRequest amortisation);
}
