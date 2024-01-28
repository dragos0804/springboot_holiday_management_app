package com.lab.model.controller;

import com.lab.model.model.DaysOffEntity;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.UserRepository;
import com.lab.model.service.DaysOffService;
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

    @Autowired
    public HomeController(UserRepository userRepository, UserService userService, DaysOffService daysOffService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.daysOffService = daysOffService;
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

        MenuItem calendar = new MenuItem();
        calendar.setName("Calendar");
        calendar.setUrl("/calendar");
        Icon rolesIcon = Icon.CALENDAR;
        rolesIcon.setColor(Icon.IconColor.INDIGO);

        calendar.setIcon(rolesIcon);
        menu.add(calendar);

        model.addAttribute("menuItems", menu);

        DaysOffEntity daysOff = new DaysOffEntity();
        model.addAttribute("daysOff",daysOff);

        UserEntity user = userService.getCurrentUser();
        List<DaysOffEntity> usersDaysOff = daysOffService.findAllByUser(user);
        model.addAttribute("usersDaysOff",usersDaysOff);

        return "home";
    }



    @PostMapping()
    public String updateDaysOff(Model model, @ModelAttribute("daysOff") DaysOffEntity daysOff) {
        logger.info("update daysOff called");
        UserEntity user = userService.getCurrentUser();
        daysOff.setUser(user);
        daysOff.setApproved(false);

        daysOffService.update(daysOff);

        return "redirect:/home";  //o sa il trimitem sa asi vada concediul
    }
}
