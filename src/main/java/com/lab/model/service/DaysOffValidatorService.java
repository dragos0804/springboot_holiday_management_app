package com.lab.model.service;

import com.lab.model.model.DaysOffEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;


import java.time.LocalDate;

@Service
public class DaysOffValidatorService {

    /**
     * Validates if the start date is not after the end date.
     *
     * @param daysOff The DaysOffEntity object to validate.
     * @return true if the start date is on or before the end date, false otherwise.
     */
    public boolean isValid(DaysOffEntity daysOff) {
        LocalDate startDate = daysOff.getStartDate();
        LocalDate endDate = daysOff.getEndDate();

        // Check if either date is null
        if (startDate == null || endDate == null) {
            return false; // or handle according to your requirements
        }

        return !startDate.isAfter(endDate);
    }
}
