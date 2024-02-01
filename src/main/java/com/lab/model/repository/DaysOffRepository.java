package com.lab.model.repository;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DaysOffRepository extends JpaRepository<DaysOffEntity, Long> {

    Optional<List<DaysOffEntity>> findAllByUser(UserEntity user);
    Optional<DaysOffEntity> findById(Long id);
}
