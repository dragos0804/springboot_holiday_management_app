package com.lab.model.repository;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaysOffRepository extends JpaRepository<DaysOffEntity, Long> {

}
