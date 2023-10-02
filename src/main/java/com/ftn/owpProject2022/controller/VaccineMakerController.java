package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.model.VaccineMaker;
import com.ftn.owpProject2022.service.UserService;
import com.ftn.owpProject2022.service.VaccineMakerService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vaccineMakers")
public class VaccineMakerController implements ServletContextAware {

    private  String bURL;
    private ServletContext servletContext;
    private UserService userService;
    private VaccineMakerService vaccineMakerService;

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath()+"/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public VaccineMakerController(ServletContext servletContext,VaccineMakerService vaccineMakerService,
                                  UserService userService){
        this.servletContext = servletContext;
        this.vaccineMakerService = vaccineMakerService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView index(HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = userService.getLoggedUser(session);
        if(loggedUser != null) {
            List<VaccineMaker> vaccineMakers = vaccineMakerService.findAll();
            ModelAndView modelAndView = new ModelAndView("vaccineMakerTemplates/vaccineMakers");
            modelAndView.addObject("vaccineMakers", vaccineMakers);
            modelAndView.addObject("loggedUser", loggedUser);
            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }
    }

    @PostMapping(value="/add")
    public void create(@RequestParam (required = true) String manufacturer, @RequestParam (required = true) String productionCountry,
                       HttpServletResponse response) throws IOException {
        String id = IdGenerator.generateId();
        List<Vaccine> vaccines = new ArrayList<>();
        VaccineMaker vaccineMaker = new VaccineMaker(id, manufacturer, productionCountry, vaccines);
        vaccineMakerService.save(vaccineMaker);
        response.sendRedirect(bURL+"vaccineMakers");
    }

    @PostMapping(value="/edit")
    public void edit(@RequestParam String id, @RequestParam String manufacturer, @RequestParam String productionCountry,
                     HttpServletResponse response) throws IOException {
        VaccineMaker vaccineMaker = vaccineMakerService.findOneById(id);
        if(vaccineMaker != null) {
            if(!manufacturer.trim().isEmpty())
                vaccineMaker.setManufacturer(manufacturer);
            if(!productionCountry.trim().isEmpty())
                vaccineMaker.setProductionCountry(productionCountry);
        }
        vaccineMakerService.update(vaccineMaker);
        response.sendRedirect(bURL+"vaccineMakers");
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam String id, HttpServletResponse response) throws IOException {
        VaccineMaker deleted = vaccineMakerService.delete(id);
        response.sendRedirect(bURL+"vaccineMakers");
    }

    @GetMapping(value="/details")
    @ResponseBody
    public ModelAndView details(@RequestParam String id, HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = userService.getLoggedUser(session);

        if(loggedUser != null) {
            VaccineMaker vaccineMaker = vaccineMakerService.findOneById(id);
            ModelAndView modelAndView = new ModelAndView("vaccineMakerTemplates/vaccineMakerDetails");
            modelAndView.addObject("vaccineMaker", vaccineMaker);
            modelAndView.addObject("loggedUser", loggedUser);

            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }

    }
}