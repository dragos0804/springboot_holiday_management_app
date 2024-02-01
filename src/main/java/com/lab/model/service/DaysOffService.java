package com.lab.model.service;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.DaysOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//    public List<DaysOffEntity> findAllByUsers(List<UserEntity> users) {
        //cautare in lista de users
//
//        users.forEach(userEntity -> userEntity.getDaysOffPerUser());
//
//        return optionalDaysOffEntities.orElse(null);
  //  }

    //TODO TEST THIS METHOD
    public List<DaysOffEntity> findAllByUsersUnaccepted(List<UserEntity> users) {
//        List<DaysOffEntity> allDaysOffUnaccepted = findAllByUsers(user);
//        for(var dayOff : allDaysOffUnaccepted)
//            if(dayOff.getIsApproved() == true)
//                allDaysOffUnaccepted.remove(dayOff);
//        return allDaysOffUnaccepted;
        return users
                .stream()
                .flatMap(user ->
                        user.getDaysOffPerUser().stream())
                .filter(daysOff -> !daysOff.getIsApproved())
                .collect(Collectors.toList());
    }
    public DaysOffEntity findById(Long id) {
        Optional<DaysOffEntity> optDaysOffEntities = daysOffRepository.findById(id);
        return optDaysOffEntities.orElse(null);
    }

}
