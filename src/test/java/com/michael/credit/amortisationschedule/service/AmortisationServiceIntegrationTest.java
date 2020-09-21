package com.michael.credit.amortisationschedule.service;

import com.michael.credit.amortisationschedule.dto.request.AmortisationRequest;
import com.michael.credit.amortisationschedule.dto.response.AmortisationResponse;
import com.michael.credit.amortisationschedule.dto.response.AmortisationScheduleResponse;
import com.michael.credit.amortisationschedule.utils.Round2Scales;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
class AmortisationServiceIntegrationTest {

    @TestConfiguration
    static class AmortisationServiceIntegrationTestContextConfiguration {

        @Bean
        public AmortisationService amortisationService() {
            return new AmortisationServiceImpl();
        }
    }

    @Autowired
    private AmortisationService amortisationService;

    @Test
    public void should_calculate_amortisation_schedule() {
        AmortisationRequest amortisationRequest = AmortisationRequest
                .builder()
                .loanAmount(Round2Scales.rint(new BigDecimal(200000)))
                .borrowingRate(Round2Scales.rint(new BigDecimal(0.97)))
                .startRedemption(Round2Scales.rint(new BigDecimal(2.0)))
                .years(5)
                .build();

        List<AmortisationScheduleResponse> amortisationScheduleResponseSet = new ArrayList<>(){{
            add(AmortisationScheduleResponse
                    .builder()
                    .rate(Round2Scales.rint(new BigDecimal(495.00)))
                    .balanceDue(Round2Scales.rint(new BigDecimal(195982.19)))
                    .amortisationPortion(Round2Scales.rint(new BigDecimal(4017.81)))
                    .interestPortion(Round2Scales.rint(new BigDecimal(1922.19)))
                    .year(1)
                    .build());
            add(AmortisationScheduleResponse
                    .builder()
                    .rate(Round2Scales.rint(new BigDecimal(495.00)))
                    .balanceDue(Round2Scales.rint(new BigDecimal(191925.21)))
                    .amortisationPortion(Round2Scales.rint(new BigDecimal(4056.98)))
                    .interestPortion(Round2Scales.rint(new BigDecimal(1883.02)))
                    .year(2)
                    .build());
            add(AmortisationScheduleResponse
                    .builder()
                    .rate(Round2Scales.rint(new BigDecimal(495.00)))
                    .balanceDue(Round2Scales.rint(new BigDecimal(187828.70)))
                    .amortisationPortion(Round2Scales.rint(new BigDecimal(4096.51)))
                    .interestPortion(Round2Scales.rint(new BigDecimal(1843.49)))
                    .year(3)
                    .build());
            add(AmortisationScheduleResponse
                    .builder()
                    .rate(Round2Scales.rint(new BigDecimal(495.00)))
                    .balanceDue(Round2Scales.rint(new BigDecimal(183692.27)))
                    .amortisationPortion(Round2Scales.rint(new BigDecimal(4136.43)))
                    .interestPortion(Round2Scales.rint(new BigDecimal(1803.57)))
                    .year(4)
                    .build());
            add(AmortisationScheduleResponse
                    .builder()
                    .rate(Round2Scales.rint(new BigDecimal(495.00)))
                    .balanceDue(Round2Scales.rint(new BigDecimal(179515.53)))
                    .amortisationPortion(Round2Scales.rint(new BigDecimal(4176.74)))
                    .interestPortion(Round2Scales.rint(new BigDecimal(1763.26)))
                    .year(5)
                    .build());
        }};

        AmortisationResponse expectedAmortisationResponse = AmortisationResponse
                .builder()
                .monthlyLoanRate(Round2Scales.rint(new BigDecimal(495.00)))
                .balanceDueAfterFixedInterestRate(Round2Scales.rint(new BigDecimal(179515.53)))
                .amortisationSchedule(amortisationScheduleResponseSet)
                .build();

        AmortisationResponse amortisationResponse = amortisationService.calculate(amortisationRequest);

        assertThat(amortisationResponse).isEqualTo(expectedAmortisationResponse);
    }
}
