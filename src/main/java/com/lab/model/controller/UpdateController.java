package com.lab.model.controller;

import com.lab.model.model.DaysOffEntity;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/approve")
public class UpdateController {

    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private UserService userService;
    private DaysOffService daysOffService;

    @Autowired
    public UpdateController(UserRepository userRepository, UserService userService, DaysOffService daysOffService) {
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


        List<UserEntity> employees = userService.getAllUsersUnderSameManager();
        List<DaysOffEntity> allDaysOffForAllUsers = daysOffService.findAllByUsersUnaccepted(employees);
        model.addAttribute("allDaysOffForAllUsers", allDaysOffForAllUsers);


        return "approve";
    }

    @PostMapping()
    public String acceptEmployeeHoliday(Model model,
                                        @ModelAttribute("daysOff") DaysOffEntity daysOff,
                                        @RequestParam(value = "actionAccept", required = false) String acceptDoer,
                                        @RequestParam(value = "actionDecline", required = false) String declineDoer) {

        logger.info("acceptEmployeeHoliday called");

        String isAccepted = "false";
        Long daysOffId = -1L;

        if(acceptDoer != null && !acceptDoer.isEmpty()) {
            String[] accepted = acceptDoer.split("-");
            isAccepted = accepted[0];
            daysOffId = Long.parseLong(accepted[1]);
        }
        else if(declineDoer != null && !declineDoer.isEmpty()){
            String[] declined = declineDoer.split("-");
            isAccepted = declined[0];
            daysOffId = Long.parseLong(declined[1]);
        }

        DaysOffEntity daysOffEntityLocal = null;
        if(daysOffId != -1L)
            daysOffEntityLocal = daysOffService.findById(daysOffId);

        boolean isAcceptedBool = isAccepted.equals("accept");

        if(isAcceptedBool)
            logger.info("Accepted");
        else
            logger.info("Declined");

        if(daysOffEntityLocal != null) {

            if(isAcceptedBool)
            {
                daysOffEntityLocal.setIsApproved(isAcceptedBool);
                daysOffService.update(daysOffEntityLocal);
            }
            else
            {
                daysOffService.delete(daysOffEntityLocal);
            }
        }

        return "redirect:/approve";
    }
}
