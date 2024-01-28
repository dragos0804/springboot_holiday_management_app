package com.lab.model.repository;

import com.lab.model.model.DaysOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DaysOffRepository extends JpaRepository<DaysOffEntity, Long> {
}
