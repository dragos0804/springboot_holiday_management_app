package com.lab.model.service;

import com.lab.model.config.util.Role;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
/* Will create the necessary constructor for the DI mechanism, but I do not recommend.
*  You should explicitly put the @Autowired annotation on the constructor. */
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optUser = userRepository.findByEmail(email);
        if(optUser.isPresent()){
            UserEntity appUser = optUser.get();
            return new User(appUser.getEmail(), appUser.getPassword(), true, true, true, true,
                    /* User Roles */
                    Objects.isNull(appUser.getRoles()) ?
                            new ArrayList<>(List.of(new SimpleGrantedAuthority("INACTIVE")))
                            : appUser.getRoles()
                            .stream()
                            .map(RoleEntity::getName)
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );
        }
        throw new UsernameNotFoundException(email);
    }

    public void register(UserEntity user) {
        /* Encrypt password */
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        /* each user saved/registered receive the AUTH role */
        roleService.findByName(Role.AUTH).ifPresent(user::addRole);
        userRepository.save(user);
    }

    public void save(UserEntity user) {
        /* Encrypt password */
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public void update(UserEntity user) {
        userRepository.save(user);
    }

    public void login(UserEntity user, Authentication authentication) {
        UserDetails userDetails = this.loadUserByUsername(user.getEmail());
        if(Objects.isNull(userDetails))
            return;

        /* Here you set your user on the current session. You can also save the user entity directly. */
        authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        // UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Optional<UserEntity> findByEmail(String adminEmail) {
        return userRepository.findByEmail(adminEmail);
    }

    public Page<UserEntity> findAll(int pageNumber, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public Page<UserEntity> findAll(Pageable page) {
        return userRepository.findAll(page);
    }
    public UserEntity findById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Optional<UserEntity> optUser = userRepository.findByEmail(authentication.getName());

            if(optUser.isPresent())
                return optUser.get();
        }
        return null;
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserEntity> getAllUsersUnderSameManager() {
        UserEntity manager = getCurrentUser();
        if(manager != null) {
            return manager.getEmployees();
        }
        return null;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    //functie care cauta ca prin useri si vede daca un user mai e manager sau nu inca
    public List<UserEntity> findNonManagerUsers(List<UserEntity> users) {
        return users.stream()
                .filter(user -> users.stream().noneMatch(otherUser -> otherUser.getManager().getId() != null && otherUser.getManager().getId().equals(user.getId())))
                .collect(Collectors.toList());
    }

    public void setManager(Long employeeId, Long managerId) {
        UserEntity employee = getUserById(employeeId);
        UserEntity manager = getUserById(managerId);

        RoleEntity approveRequestsRole = roleService.findByName(Role.APPROVE_DAYS_OFF_REQUEST).get();
        RoleEntity setVacancyDaysRole = roleService.findByName(Role.SET_VACANCY_DAYS_NUMBER).get();

        List<UserEntity> users = getAllUsers();

        if (employeeId.equals(managerId)) {
            // If making the employee a director
            users.stream()
                    .filter(user -> user.getManager() != null && user.getManager().equals(user))
                    .findFirst()
                    .ifPresent(oldDirector -> {
                        oldDirector.setManager(employee);
                        update(oldDirector);
                    });
        } else {
            // If changing the manager of the employee
            if (employee.getManager() != null && employee.getManager().equals(employee)) {
                manager.setManager(manager);
                update(manager);
            }
        }

        if(!manager.getRoles().contains(approveRequestsRole))
            manager.getRoles().add(approveRequestsRole);
        if(!manager.getRoles().contains(setVacancyDaysRole))
            manager.getRoles().add(setVacancyDaysRole);

        employee.setManager(manager);
        update(employee);


        //functie care verifica daca noul manager are rolurile corespunzatoare, daca nu, i se dau rolurile
        List<UserEntity> nonManagers = findNonManagerUsers(users);
        for(UserEntity nonManager : nonManagers)
        {
            nonManager.getRoles().remove(approveRequestsRole);
            nonManager.getRoles().remove(setVacancyDaysRole);
            update(nonManager);
        }
    }
}
