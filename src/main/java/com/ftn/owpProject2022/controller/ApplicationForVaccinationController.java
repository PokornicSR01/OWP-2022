package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.model.ApplicationForVaccination;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.ApplicationForVaccinationService;
import com.ftn.owpProject2022.service.UserService;
import com.ftn.owpProject2022.service.VaccineService;
import com.ftn.owpProject2022.utilities.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Controller
@RequestMapping(value = "/applications")
public class ApplicationForVaccinationController implements ServletContextAware {
    private  String bURL;
    private ServletContext servletContext;
    private ApplicationForVaccinationService applicationForVaccinationService;
    private VaccineService vaccineService;
    private UserService userService;

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath()+"/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public ApplicationForVaccinationController(ServletContext servletContext, ApplicationForVaccinationService application,
                                               VaccineService vaccineService, UserService userService){
        this.servletContext = servletContext;
        this.applicationForVaccinationService = application;
        this.vaccineService = vaccineService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView index(@RequestParam(value = "firstName", required = false) String firstName,
                              @RequestParam(value = "lastName", required = false) String lastName,
                              @RequestParam(value = "UPIN", required = false) String UPIN,
                              HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = userService.getLoggedUser(session);
        if(loggedUser != null) {
            ModelAndView modelAndView = new ModelAndView("applicationForVaccinesTemplates/applicationsForVaccines");
            modelAndView.addObject("loggedUser", loggedUser);

            if(firstName == null && lastName == null && UPIN == null){
                List<ApplicationForVaccination> applications = applicationForVaccinationService.findAll();
                modelAndView.addObject("applications", applications);
            } else {
                List<ApplicationForVaccination> applications = applicationForVaccinationService.searchApplication(firstName, lastName, UPIN);
                modelAndView.addObject("applications", applications);
            }
            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }
    }

    @PostMapping(value="/approve")
    public void approve(@RequestParam String id, HttpServletResponse response, HttpSession httpSession) throws IOException {
        User user = applicationForVaccinationService.findOneById(id).getPatient();

        applicationForVaccinationService.approve(id);
        applicationForVaccinationService.deleteUsersApplications(user.getId());

        response.sendRedirect(bURL +  "applications");
    }

    @PostMapping(value="/decline")
    public void decline(@RequestParam String id, HttpServletResponse response, HttpSession httpSession) throws IOException {
        applicationForVaccinationService.decline(id);

        response.sendRedirect(bURL +  "applications");
    }

    @PostMapping(value="/cancel")
    public void cancel(@RequestParam String id, HttpServletResponse response, HttpSession httpSession) throws IOException {
        applicationForVaccinationService.delete(id);

        response.sendRedirect(bURL +  "users/profile");
    }

    @GetMapping(value="/apply")
    public ModelAndView applicationForVaccine(@RequestParam String id,
                                    HttpServletResponse response, HttpSession httpSession) throws IOException {
        User loggedUser = userService.getLoggedUser(httpSession);

        if(loggedUser != null) {
            Vaccine vaccine = vaccineService.findOneById(id);

            ModelAndView modelAndView = new ModelAndView("applicationForVaccinesTemplates/applyForVaccine");
            modelAndView.addObject("vaccine", vaccine);
            modelAndView.addObject("loggedUser", loggedUser);

            return  modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed");
            return null;
        }

    }

    @PostMapping(value = "/apply")
    public void applyForVaccination(@RequestParam String id,
                                    @RequestParam String appointmentTimeString,
                                    HttpServletResponse response, HttpSession httpSession) throws IOException {

        if (appointmentTimeString != null) {
            LocalDateTime appointmentTime = LocalDateTime.parse(appointmentTimeString);
            User user = userService.getLoggedUser(httpSession);
            user.setApplicationList(applicationForVaccinationService.findUsersApplications(user.getId()));
            Vaccine vaccine = vaccineService.findOneById(id);
            String applicationId = IdGenerator.generateId();

            ApplicationForVaccination application = new ApplicationForVaccination(applicationId, user, vaccine, appointmentTime, false);

            int[] intervals = {0, 3, 6, 3};

            if (appointmentTime.isAfter(LocalDateTime.now()) && vaccine.getAvailableQuantity() > 0 && applicationForVaccinationService.findUsersFinishedApplications(user.getId()).size() < 4) {
                List<ApplicationForVaccination> userApplications = applicationForVaccinationService.findUsersFinishedApplications(user.getId());

                if (userApplications.isEmpty() || isEligibleForVaccination(userApplications, application, intervals[userApplications.size()])) {
                    applicationForVaccinationService.save(application);
                }
            }

            response.sendRedirect(bURL + "vaccines");
        } else {
            response.sendRedirect(bURL + "vaccines");
        }
    }

    private boolean isEligibleForVaccination(List<ApplicationForVaccination> applications, ApplicationForVaccination application,  int interval) {
        if (applications.isEmpty()) {
            return true;
        }

        LocalDateTime lastApplicationTime = applications.get(applications.size() - 1).getAppointmentTime();
        LocalDateTime nextAllowedTime = lastApplicationTime.plusMonths(interval);

        return application.getAppointmentTime().isAfter(nextAllowedTime) || application.getAppointmentTime().isEqual(nextAllowedTime);
    }


}
