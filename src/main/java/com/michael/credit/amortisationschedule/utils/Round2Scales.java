package com.michael.credit.amortisationschedule.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Round2Scales
{
    public static BigDecimal rint( BigDecimal bd )
    {
        return bd.setScale(2, RoundingMode.HALF_UP);
    }
}