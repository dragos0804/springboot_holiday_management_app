package com.lab.model.service;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.DaysOffRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DaysOffService {
    private DaysOffRepository daysOffRepository;
    public static List<LocalDate> nationalHolidays = Arrays.asList(
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 1, 2),
            LocalDate.of(2024, 1, 6),
            LocalDate.of(2024, 1, 7),
            LocalDate.of(2024, 1, 24),
            LocalDate.of(2024, 5, 3),
            LocalDate.of(2024, 5, 4),
            LocalDate.of(2024, 5, 5),
            LocalDate.of(2024, 5, 6),
            LocalDate.of(2024, 5, 1),
            LocalDate.of(2024, 6, 1),
            LocalDate.of(2024, 6, 23),
            LocalDate.of(2024, 6, 24),
            LocalDate.of(2024, 8, 15),
            LocalDate.of(2024, 11, 30),
            LocalDate.of(2024, 12, 1),
            LocalDate.of(2024, 12, 25),
            LocalDate.of(2024, 12, 26)
    );

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
    public List<DaysOffEntity> findAllByUsersUnaccepted(List<UserEntity> users) {
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
    public void deleteById(DaysOffEntity daysOff) {
        daysOffRepository.delete(daysOff);
    }
    @Transactional
    public Long getUserIdByDayOffId(Long requestId) {
        // Retrieve the vacation request by ID
        Optional<DaysOffEntity> optionalRequest = daysOffRepository.findById(requestId);

        if (optionalRequest.isPresent()) {
            DaysOffEntity vacationRequest = optionalRequest.get();

            // Identify the user who sent the request
            UserEntity user = vacationRequest.getUser();

            if (user != null) {
                return user.getId();
            }
        }
        return null;
    }
}
