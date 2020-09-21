package com.michael.credit.amortisationschedule.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Builder
@Data
@AllArgsConstructor
public class AmortisationRequest {

    @NotNull
    @Positive(message = "Loan amount must be positive")
    private BigDecimal loanAmount;

    @NotNull
    private BigDecimal borrowingRate;

    @NotNull
    @Positive(message = "Start redemption must be positive")
    private BigDecimal startRedemption;

    @NotNull
    @Positive(message = "Year must be positive")
    private int years;
}
