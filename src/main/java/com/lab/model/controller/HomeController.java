package com.lab.model.controller;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.UserRepository;
import com.lab.model.service.DaysOffService;
import com.lab.model.service.DaysOffValidatorService;
import com.lab.model.service.UserService;
import com.lab.model.util.Icon;
import com.lab.model.util.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private UserService userService;

    private DaysOffService daysOffService;

    private DaysOffValidatorService daysOffValidatorService;


    @Autowired
    public HomeController(UserRepository userRepository, UserService userService, DaysOffService daysOffService, DaysOffValidatorService daysOffValidatorService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.daysOffService = daysOffService;
        this.daysOffValidatorService = daysOffValidatorService;
    }

    @GetMapping()
    public String open(Model model, Authentication authentication){
        List<MenuItem> menu = new ArrayList<>();

        MenuItem home = new MenuItem();
        home.setName("Home");
        Icon homeIcon = Icon.HOME;
        homeIcon.setColor(Icon.IconColor.INDIGO);

        home.setIcon(homeIcon);
        home.setUrl("/home");
        menu.add(home);

        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("APPROVE_DAYS_OFF_REQUEST"))) {
            MenuItem approve = new MenuItem();

            approve.setName("Approve");
            approve.setUrl("/approve");
            Icon rolesIcon = Icon.CALENDAR;
            rolesIcon.setColor(Icon.IconColor.INDIGO);

            approve.setIcon(rolesIcon);
            menu.add(approve);
        }

        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("SET_VACANCY_DAYS_NUMBER"))) {
            MenuItem vacancy = new MenuItem();

            vacancy.setName("Vacancy");
            vacancy.setUrl("/setVacancy");
            Icon rolesIcon = Icon.DOUBLE_ARROW_RIGHT;
            rolesIcon.setColor(Icon.IconColor.INDIGO);

            vacancy.setIcon(rolesIcon);
            menu.add(vacancy);
        }


        model.addAttribute("menuItems", menu);

        DaysOffEntity daysOff = new DaysOffEntity();
        model.addAttribute("daysOff",daysOff);

        UserEntity user = userService.getCurrentUser();
        model.addAttribute("currentUser", user);

        List<DaysOffEntity> usersDaysOff = daysOffService.findAllByUser(user);
        model.addAttribute("usersDaysOff",usersDaysOff);

        return "home";
    }

    @PostMapping()
    public String updateDaysOff(Model model, @ModelAttribute("daysOff") DaysOffEntity daysOff) {
        logger.info("update daysOff called");

        if (!daysOffValidatorService.isValid(daysOff)) {
            model.addAttribute("dateValidationError", "Start date must be on or before end date.");
            model.addAttribute("daysOff", daysOff); // Add this line to return the form with user data
            return "/home"; // Redirect back to the form with an error message
        }

        UserEntity user = userService.getCurrentUser();
        daysOff.setUser(user);
        daysOff.setIsApproved(false);

        daysOffService.update(daysOff);

        return "redirect:/home"; // Redirect after successful update
    }

}
