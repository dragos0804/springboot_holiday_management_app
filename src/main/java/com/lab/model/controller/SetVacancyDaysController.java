package com.lab.model.controller;

import com.lab.model.model.UserEntity;
import com.lab.model.repository.UserRepository;
import com.lab.model.service.UserService;
import com.lab.model.util.Icon;
import com.lab.model.util.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/setVacancy")
public class SetVacancyDaysController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private UserService userService;

    @Autowired
    public SetVacancyDaysController(UserService userService) {
        this.userService = userService;
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

        MenuItem approve = new MenuItem();
        approve.setName("Approve");
        approve.setUrl("/approve");
        Icon approveicon = Icon.CALENDAR;
        approveicon.setColor(Icon.IconColor.INDIGO);
        approve.setIcon(approveicon);
        menu.add(approve);


        MenuItem vacancy = new MenuItem();
        vacancy.setName("Vacancy");
        vacancy.setUrl("/setVacancy");
        Icon vacancyIcon = Icon.DOUBLE_ARROW_RIGHT;
        vacancyIcon.setColor(Icon.IconColor.INDIGO);
        vacancy.setIcon(vacancyIcon);
        menu.add(vacancy);

        model.addAttribute("menuItems", menu);

        List<UserEntity> employeesUnderThisManager = userService.getAllUsersUnderSameManager();
        model.addAttribute("employeesUnderThisManager", employeesUnderThisManager);


        return "setVacancy";
    }

    @PostMapping()
    public String acceptEmployeeHoliday(Model model,@RequestParam("employeeId") Long employeeId,
                                        @RequestParam("freeDays") int freeDays)
    {
        UserEntity user = userService.getUserById(employeeId);
        user.setNrOfDaysOff(freeDays);
        userService.update(user);

        return "redirect:/setVacancy";
    }


}
