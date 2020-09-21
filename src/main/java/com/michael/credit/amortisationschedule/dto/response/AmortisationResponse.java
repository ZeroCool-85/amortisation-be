package com.michael.credit.amortisationschedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AmortisationResponse {
    private BigDecimal monthlyLoanRate;
    private BigDecimal balanceDueAfterFixedInterestRate;
    private List<AmortisationScheduleResponse> amortisationSchedule;

    public AmortisationResponse() {
        super();
    }
}
