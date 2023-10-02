package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.enums.Role;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.model.VaccineMaker;
import com.ftn.owpProject2022.service.ApplicationForVaccinationService;
import com.ftn.owpProject2022.service.UserService;
import com.ftn.owpProject2022.service.VaccineMakerService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "vaccines")
public class VaccineController  implements ServletContextAware {
    private  String bURL;
    private ServletContext servletContext;
    private UserService userService;
    private VaccineService vaccineService;
    private ApplicationForVaccinationService application;
    private VaccineMakerService vaccineMakerService;

    @Autowired
    public VaccineController(ServletContext servletContext, UserService userService,
                             VaccineService vaccineService, VaccineMakerService vaccineMakerService,
                             ApplicationForVaccinationService application) {
        this.servletContext = servletContext;
        this.userService = userService;
        this.vaccineService = vaccineService;
        this.vaccineMakerService = vaccineMakerService;
        this.application = application;
    }

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath()+"/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @GetMapping
    public ModelAndView index(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "manufacturer", required = false) String manufacturer,
                              @RequestParam(value = "country", required = false) String country,
                              @RequestParam(value = "minQuantity", required = false) Integer minQuantity,
                              @RequestParam(value = "maxQuantity", required = false) Integer maxQuantity,
                              @RequestParam(value = "sortAttribute", defaultValue = "default") String sortAttribute,
                              HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = userService.getLoggedUser(session);
        if(loggedUser != null){
            List<Vaccine> vaccines = new ArrayList<>();
            List<VaccineMaker> vaccineMakers = vaccineMakerService.findAll();
            if(loggedUser.getRole() == Role.PATIENT){
                vaccines = vaccineService.findAllAvaibleVaccines();
            } else {
                if (name != null || manufacturer != null || country != null || minQuantity != null || maxQuantity != null) {
                    vaccines = vaccineService.searchVaccine(name, manufacturer, country, minQuantity, maxQuantity);
                } else {
                    switch (sortAttribute) {
                        case "name_asc":
                            vaccines = vaccineService.sortByName("ASC");
                            break;
                        case "name_desc":
                            vaccines = vaccineService.sortByName("DESC");
                            break;
                        case "manufacturer_asc":
                            vaccines = vaccineService.sortByManufacturer("ASC");
                            break;
                        case "manufacturer_desc":
                            vaccines = vaccineService.sortByManufacturer("DESC");
                            break;
                        case "country_asc":
                            vaccines = vaccineService.sortByCountry("ASC");
                            break;
                        case "country_desc":
                            vaccines = vaccineService.sortByCountry("DESC");
                            break;
                        case "quantity_asc":
                            vaccines = vaccineService.sortByQuantity("ASC");
                            break;
                        case "quantity_desc":
                            vaccines = vaccineService.sortByQuantity("DESC");
                            break;
                        default:
                            vaccines = vaccineService.findAll();
                            break;
                    }
                }
            }

            ModelAndView modelAndView = new ModelAndView("vaccineTemplates/vaccines");
            modelAndView.addObject("loggedUser", loggedUser);
            modelAndView.addObject("vaccines", vaccines);
            modelAndView.addObject("vaccineMakers", vaccineMakers);

            boolean canUserGetMoreVaccines = application.findUsersFinishedApplications(loggedUser.getId()).size() < 4;
            modelAndView.addObject("canUserGetMoreVaccines", canUserGetMoreVaccines);

            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }
    }

    @PostMapping(value="/add")
    public void create(@RequestParam String name, @RequestParam String makerId,
                       HttpServletResponse response) throws IOException {
        int avaiableQuantity = 0;
        String vaccineId = IdGenerator.generateId();
        VaccineMaker vaccineMaker = vaccineMakerService.findOneById(makerId);
        Vaccine vaccine = new Vaccine(vaccineId,name, avaiableQuantity, vaccineMaker);
        vaccineService.save(vaccine);
        response.sendRedirect(bURL+"vaccines");
    }

    @PostMapping(value="/edit")
    public void Edit(@RequestParam String id, @RequestParam String name, @RequestParam String makerId,
                     HttpServletResponse response) throws IOException {
        Vaccine vaccine = vaccineService.findOneById(id);
        if(vaccine != null) {
            if(!name.trim().isEmpty())
                vaccine.setName(name);
            if(!makerId.trim().isEmpty()) {
                VaccineMaker vaccineMaker = vaccineMakerService.findOneById(makerId);
                vaccine.setVaccineMaker(vaccineMaker);
            }
        }
        vaccineService.update(vaccine);
        response.sendRedirect(bURL + "vaccines/details?id=" + id);
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam String id, HttpServletResponse response) throws IOException {
        vaccineService.delete(id);
        response.sendRedirect(bURL+"vakcine");
    }

    @GetMapping(value="/details")
    @ResponseBody
    public ModelAndView details(@RequestParam String id, HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = userService.getLoggedUser(session);

        if(loggedUser != null) {
            Vaccine vaccine = vaccineService.findOneById(id);
            List<VaccineMaker> vaccineMakers = vaccineMakerService.findAll();
            ModelAndView modelAndView = new ModelAndView("vaccineTemplates/vaccineDetails");
            modelAndView.addObject("vaccine", vaccine);
            modelAndView.addObject("vaccineMakers", vaccineMakers);
            modelAndView.addObject("loggedUser", loggedUser);

            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }

    }
}
