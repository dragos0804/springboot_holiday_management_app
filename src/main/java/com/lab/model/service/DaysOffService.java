package com.lab.model.service;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.repository.DaysOffRepository;

public class DaysOffService {
    private DaysOffRepository daysOffRepository;

    public void update(DaysOffEntity daysOff) {
        daysOffRepository.save(daysOff);
    }
}
