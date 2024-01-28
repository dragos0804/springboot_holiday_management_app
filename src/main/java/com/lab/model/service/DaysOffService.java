package com.lab.model.service;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.DaysOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DaysOffService {
    private DaysOffRepository daysOffRepository;

    @Autowired
    public DaysOffService(DaysOffRepository daysOffRepository){
        this.daysOffRepository = daysOffRepository;
    }

    public void update(DaysOffEntity daysOff) {
        daysOffRepository.save(daysOff);
    }

    public void delete(DaysOffEntity daysOff) {
        daysOffRepository.delete(daysOff);
    }

    public List<DaysOffEntity> findAllByUser(UserEntity user) {
        Optional<List<DaysOffEntity>> optionalDaysOffEntities = daysOffRepository.findAllByUser(user);
        return optionalDaysOffEntities.orElse(null);
    }

}
