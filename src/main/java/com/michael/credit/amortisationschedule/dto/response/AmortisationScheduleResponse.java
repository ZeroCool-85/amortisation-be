package com.michael.credit.amortisationschedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class AmortisationScheduleResponse {
    private int year;
    private BigDecimal rate;
    private BigDecimal interestPortion;
    private BigDecimal amortisationPortion;
    private BigDecimal balanceDue;

    public AmortisationScheduleResponse() {
        super();
    }
}
