package com.lab.model.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name="app_user")
public class UserEntity implements Comparable<UserEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "username")
    private String username;

    /* Force the validation of the email on the database level too */
    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255) CHECK (email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "nr_of_days_off")
    private Integer nrOfDaysOff;

    @Transient
    private String repeatPassword;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "app_users_roles",
            joinColumns = @JoinColumn(
                    name = "app_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> roles = new ArrayList<>();

    @ManyToOne
    private UserEntity manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.EAGER) //owner-ul relatiei
    private List<UserEntity> employees;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<DaysOffEntity> daysOffPerUser;

    public void addRole(RoleEntity role) {
        roles.add(role);
    }

    @Override
    public int compareTo(UserEntity o) {
        return this.getId().compareTo(o.getId());
    }
}
