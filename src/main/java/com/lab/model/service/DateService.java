package com.lab.model.service;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.DaysOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DateService {

    public boolean isWeekend(LocalDate date) {
        // Check if the given date is a weekend (Saturday or Sunday)
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public boolean isNationalHoliday(LocalDate date, List<LocalDate> nationalHolidays) {
        // Check if the given date is a national holiday
        return nationalHolidays.contains(date);
    }

    public int calculateBusinessDays(LocalDate startDate, LocalDate endDate, List<LocalDate> nationalHolidays) {
        int businessDays = 0;

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (!isWeekend(currentDate) && !isNationalHoliday(currentDate, nationalHolidays)) {
                businessDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return businessDays;
    }
}
