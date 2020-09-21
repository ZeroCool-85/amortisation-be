package com.michael.credit.amortisationschedule.service;

import com.michael.credit.amortisationschedule.dto.request.AmortisationRequest;
import com.michael.credit.amortisationschedule.dto.response.AmortisationResponse;
import com.michael.credit.amortisationschedule.dto.response.AmortisationScheduleResponse;
import com.michael.credit.amortisationschedule.utils.Round2Scales;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmortisationServiceImpl implements AmortisationService {

    public AmortisationResponse calculate(AmortisationRequest amortisation) {
        BigDecimal decimalBorrowingRate = amortisation.getBorrowingRate().divide(new BigDecimal(100));
        BigDecimal decimalStartRedemption = amortisation.getStartRedemption().divide(new BigDecimal(100));
        BigDecimal ratePerMonth = ((decimalBorrowingRate.add(decimalStartRedemption))
                .multiply(amortisation.getLoanAmount()))
                .divide(new BigDecimal(12), RoundingMode.HALF_DOWN);
        BigDecimal endLoanAmount = amortisation.getLoanAmount();

        List<AmortisationScheduleResponse> amortisationScheduleResponseSet = new ArrayList<>();

        amortisationScheduleResponseSet = calculateYearsRecursive(endLoanAmount, decimalBorrowingRate, ratePerMonth, amortisation.getYears(), amortisationScheduleResponseSet);
        endLoanAmount = amortisationScheduleResponseSet.get(amortisationScheduleResponseSet.size() - 1).getBalanceDue();

        return AmortisationResponse
                .builder()
                .amortisationSchedule(amortisationScheduleResponseSet)
                .monthlyLoanRate(Round2Scales.rint(ratePerMonth))
                .balanceDueAfterFixedInterestRate(Round2Scales.rint(endLoanAmount))
                .build();
    }

    private List<AmortisationScheduleResponse> calculateYearsRecursive(BigDecimal endLoanAmount, BigDecimal decimalBorrowingRate, BigDecimal ratePerMonth, int years, List<AmortisationScheduleResponse> amortisationScheduleResponseSet) {

        int actualYear = amortisationScheduleResponseSet.size() > 0 ? amortisationScheduleResponseSet.get(amortisationScheduleResponseSet.size() -1).getYear() + 1 : 1;

        if(actualYear > years || endLoanAmount.compareTo(new BigDecimal(0)) == 0) return amortisationScheduleResponseSet;

        BigDecimal yearlyTaxes = new BigDecimal(0);
        BigDecimal yearlyAmortisation = new BigDecimal(0);
        for(int i = 0; 12 > i; i++ ) {
            BigDecimal monthlyTaxes = Round2Scales.rint(endLoanAmount.multiply(decimalBorrowingRate).divide(new BigDecimal(12), RoundingMode.HALF_DOWN));
            BigDecimal monthlyAmortisation = Round2Scales.rint(ratePerMonth.subtract(monthlyTaxes));
            endLoanAmount = endLoanAmount.subtract(monthlyAmortisation);

            if(endLoanAmount.compareTo(new BigDecimal(0)) == 0 || endLoanAmount.compareTo(new BigDecimal(0)) < 0) {
                endLoanAmount = endLoanAmount.multiply(new BigDecimal(-1));
                BigDecimal percent = endLoanAmount.divide(ratePerMonth, RoundingMode.HALF_UP);

                monthlyTaxes = monthlyTaxes.multiply(percent);
                monthlyAmortisation = monthlyAmortisation.multiply(percent);

                yearlyTaxes = yearlyTaxes.add(monthlyTaxes);
                yearlyAmortisation = yearlyAmortisation.add(monthlyAmortisation);
                endLoanAmount = endLoanAmount.subtract(endLoanAmount);
                break;
            }
            yearlyTaxes = yearlyTaxes.add(monthlyTaxes);
            yearlyAmortisation = yearlyAmortisation.add(monthlyAmortisation);
        }
        amortisationScheduleResponseSet.add(
                AmortisationScheduleResponse
                        .builder()
                        .year(actualYear)
                        .interestPortion(Round2Scales.rint(yearlyTaxes))
                        .amortisationPortion(Round2Scales.rint(yearlyAmortisation))
                        .balanceDue(Round2Scales.rint(endLoanAmount))
                        .rate(Round2Scales.rint(ratePerMonth))
                        .build()
        );
        return calculateYearsRecursive(endLoanAmount, decimalBorrowingRate, ratePerMonth, years, amortisationScheduleResponseSet);
    }
}
