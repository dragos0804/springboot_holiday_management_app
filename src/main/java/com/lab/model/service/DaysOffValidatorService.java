package com.lab.model.service;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.repository.DaysOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

@Service
public class DaysOffValidatorService {

    /**
     * Validates if the start date is not after the end date.
     *
     * @param daysOff The DaysOffEntity object to validate.
     * @return true if the start date is on or before the end date, false otherwise.
     */
    private DateService dateService;
    @Autowired
    public DaysOffValidatorService(DateService dateService){
        this.dateService = dateService;
    }
    public boolean isValidStartDateNotGraterThanEndDate(DaysOffEntity daysOff) {
        LocalDate startDate = daysOff.getStartDate();
        LocalDate endDate = daysOff.getEndDate();

        // Check if either date is null
        if (startDate == null || endDate == null) {
            return false; // or handle according to your requirements
        }

        return !startDate.isAfter(endDate);
    }
    public int isValidNumberOfDays(DaysOffEntity daysOff) {
        return dateService.calculateBusinessDays(daysOff.getStartDate(), daysOff.getEndDate(), DaysOffService.nationalHolidays);
    }
}
