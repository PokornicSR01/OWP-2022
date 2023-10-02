package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.enums.Role;
import com.ftn.owpProject2022.model.ApplicationForVaccination;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.ApplicationForVaccinationService;
import com.ftn.owpProject2022.service.UserService;
import com.ftn.owpProject2022.utilities.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController implements ServletContextAware {
    private  String bURL;
    private ServletContext servletContext;
    private UserService userService;
    private ApplicationForVaccinationService applicationService;
    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath()+"/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public UserController(ServletContext servletContext, UserService userService,ApplicationForVaccinationService applicationService ) {
        this.servletContext = servletContext;
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping(value = "/login")
    public void getLogin(@RequestParam String email, @RequestParam String passowrd,
                         HttpSession session, HttpServletResponse response) throws IOException {
        postLogin(email, passowrd, session, response);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public void postLogin(@RequestParam String email, @RequestParam String password,
                          HttpSession session, HttpServletResponse response) throws IOException {

        User loggedUser = userService.findOneByEmailAndPassword(email, password);
        List<ApplicationForVaccination> applications = applicationService.findUsersApplications(loggedUser.getId());
        loggedUser.setApplicationList(applications);

        if(loggedUser !=null){
            session.setAttribute("user", loggedUser);
            response.sendRedirect(bURL + "vaccines");
        } else {
            response.sendRedirect(bURL + "loginForm.html");
        }
    }

    @GetMapping(value="/logout")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = (User) request.getSession().getAttribute("user");

        request.getSession().removeAttribute("user");
        request.getSession().invalidate();
        response.sendRedirect(bURL + "loginForm.html");
    }

    @PostMapping(value = "/registration")
    public void registration(@RequestParam(required = true) String firstName, @RequestParam(required = true) String lastName,
                             @RequestParam(required = true) String email, @RequestParam(required = true) String password,
                             @RequestParam(required = true) String address, @RequestParam(required = true) String dateOfBirthString,
                             @RequestParam(required = true) String uniquePersonalIdentificationNumber,
                             @RequestParam(required = true) String phoneNumber,
                             @RequestParam(required = true) String confirmedPassword,
                             HttpSession session, HttpServletResponse response) throws IOException {

        if(email.contains("@email.com") || email.contains("@gmail.com")){
            if(password.equals(confirmedPassword)) {
                LocalDate registrationDate = LocalDate.now();
                List<Vaccine> vaccines = new ArrayList<>();
                List<ApplicationForVaccination> applications = new ArrayList<>();
                Role role = Role.PATIENT;
                String id = IdGenerator.generateId();
                LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString);
                User user = new User(id, firstName, lastName, email, password, address
                        , dateOfBirth, uniquePersonalIdentificationNumber, phoneNumber,
                        registrationDate, role, vaccines, applications);
                userService.save(user);
                postLogin(email, password, session, response);
            }
        }
    }

    @PostMapping(value="/edit")
    public void edit(@RequestParam String id, @RequestParam String firstName,@RequestParam String lastName,
                     @RequestParam String email, @RequestParam String address,@RequestParam String phoneNumber,
                     @RequestParam String password, @RequestParam String confirmedPassword,
                     HttpSession session, HttpServletResponse response) throws IOException {
        User user = userService.findOneById(id);
        if(user != null) {
            if(password.equals(confirmedPassword) && !password.equals("")){
                user.setPassword(password);
            }
            if(!firstName.isEmpty()){
                user.setFirstName(firstName);
            }
            if(!lastName.isEmpty()){
                user.setLastName(lastName);
            }
            if(email.contains("@email.com") || email.contains("@gmail.com")){
                user.setEmail(email);
            }
            if(!address.isEmpty()){
                user.setAddress(address);
            }
            if(!phoneNumber.isEmpty()){
                user.setPhoneNumber(phoneNumber);
            }
        }
        userService.update(user);
        session.setAttribute("user", user);
        response.sendRedirect(bURL+"users/profile" + "?id=" + id);
    }


    @GetMapping(value="/profile")
    @ResponseBody
    public ModelAndView profile(HttpSession session ,HttpServletResponse  response) throws IOException {

        User loggedUser = userService.getLoggedUser(session);

        if(loggedUser != null){
            List<ApplicationForVaccination> applications = applicationService.findUsersApplications(loggedUser.getId());
            loggedUser.setApplicationList(applications);

            ModelAndView modelAndView = new ModelAndView("userTemplates/userProfile");
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("loggedUser", loggedUser);

            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }
    }
}
