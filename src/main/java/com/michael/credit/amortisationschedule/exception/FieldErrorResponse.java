package com.michael.credit.amortisationschedule.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class FieldErrorResponse {
    private int status;
    private Map<String, String> errors;

    public FieldErrorResponse() {
        super();
    }
}
