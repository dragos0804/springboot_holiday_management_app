package com.lab.model.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA Entities
 */
@Entity
@Table(name="days_off")
@Getter
@Setter
public class DaysOffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="is_approved")
    private Boolean isApproved;

    @Column(name="message")
    private String message;

    @ManyToOne
    private UserEntity user;
}
