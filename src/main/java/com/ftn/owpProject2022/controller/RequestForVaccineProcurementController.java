package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.enums.RequestType;
import com.ftn.owpProject2022.model.RequestForVaccineProcurement;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.RequestForVaccineProcurementService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping(value = "/requests")
public class RequestForVaccineProcurementController implements ServletContextAware {
    private  String bURL;
    private ServletContext servletContext;
    private RequestForVaccineProcurementService requestForVaccineProcurementService;
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
    public RequestForVaccineProcurementController(ServletContext servletContext, RequestForVaccineProcurementService requestForVaccineProcurementService,
                                                  UserService userService, VaccineService vaccineService) {
         this.servletContext = servletContext;
         this.requestForVaccineProcurementService = requestForVaccineProcurementService;
         this.userService = userService;
         this.vaccineService = vaccineService;
    }

    @GetMapping
    public ModelAndView index(HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = userService.getLoggedUser(session);

        if(loggedUser != null){
            List<RequestForVaccineProcurement> requests = requestForVaccineProcurementService.findAllSent();
            List<RequestForVaccineProcurement> requestss = requestForVaccineProcurementService.findAllReturned();
            List<RequestForVaccineProcurement> declinedRequestss = requestForVaccineProcurementService.findAllRejected();
            List<Vaccine> vaccines = vaccineService.findAll();

            ModelAndView modelAndView = new ModelAndView("requestTemplates/requests");
            modelAndView.addObject("requests", requests);
            modelAndView.addObject("requestss", requestss);
            modelAndView.addObject("vaccines", vaccines);
            modelAndView.addObject("user", loggedUser);
            modelAndView.addObject("loggedUser", loggedUser);
            modelAndView.addObject("declinedRequestss", declinedRequestss);

            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }
    }

    @GetMapping("/update")
    @ResponseBody
    public ModelAndView getUpdate(@RequestParam String id,
                       HttpSession session, HttpServletResponse response) throws IOException {
        User user = userService.getLoggedUser(session);
        List<RequestForVaccineProcurement> requests = requestForVaccineProcurementService.findAllSent();
        List<RequestForVaccineProcurement> requestss = requestForVaccineProcurementService.findAllReturned();
        RequestForVaccineProcurement request = requestForVaccineProcurementService.findOneById(id);
        List<Vaccine> vaccines = vaccineService.findAll();

        ModelAndView modelAndView = new ModelAndView("requestTemplates/requests");
        modelAndView.addObject("requests", requests);
        modelAndView.addObject("requestss", requestss);
        modelAndView.addObject("vaccines", vaccines);
        modelAndView.addObject("editRequest", request);
        modelAndView.addObject("user", user);
        modelAndView.addObject("loggedUser", user);

        return modelAndView;
    }

    @GetMapping("/return")
    @ResponseBody
    public ModelAndView getReturn(@RequestParam String id,
                                  HttpSession session, HttpServletResponse response) throws IOException {
        User user = userService.getLoggedUser(session);
        List<RequestForVaccineProcurement> requests = requestForVaccineProcurementService.findAllSent();
        List<RequestForVaccineProcurement> requestss = requestForVaccineProcurementService.findAllReturned();
        RequestForVaccineProcurement request = requestForVaccineProcurementService.findOneById(id);
        List<Vaccine> vaccines = vaccineService.findAll();

        ModelAndView modelAndView = new ModelAndView("requestTemplates/requests");
        modelAndView.addObject("requests", requests);
        modelAndView.addObject("requestss", requestss);
        modelAndView.addObject("vaccines", vaccines);
        modelAndView.addObject("returning", true);
        modelAndView.addObject("returnRequest", request);
        modelAndView.addObject("user", user);
        modelAndView.addObject("loggedUser", user);

        return modelAndView;
    }

    @GetMapping("/decline")
    @ResponseBody
    public ModelAndView getDecline(@RequestParam String id,
                                  HttpSession session, HttpServletResponse response) throws IOException {
        User user = userService.getLoggedUser(session);
        List<RequestForVaccineProcurement> requests = requestForVaccineProcurementService.findAllSent();
        List<RequestForVaccineProcurement> requestss = requestForVaccineProcurementService.findAllReturned();
        RequestForVaccineProcurement request = requestForVaccineProcurementService.findOneById(id);
        List<Vaccine> vaccines = vaccineService.findAll();

        ModelAndView modelAndView = new ModelAndView("requestTemplates/requests");
        modelAndView.addObject("requests", requests);
        modelAndView.addObject("requestss", requestss);
        modelAndView.addObject("vaccines", vaccines);
        modelAndView.addObject("returning", true);
        modelAndView.addObject("declineRequest", request);
        modelAndView.addObject("user", user);
        modelAndView.addObject("loggedUser", user);

        return modelAndView;
    }

    @PostMapping("/create")
    public void create(@RequestParam  String reason, @RequestParam Integer count, @RequestParam String vaccineId,
                       HttpSession session, HttpServletResponse response) throws IOException {
        Vaccine vaccine = vaccineService.findOneById(vaccineId);
        String comment = "";
        RequestType requestType = RequestType.SENT;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String formattedDateTime = time.format(formatter);

        String requestId = IdGenerator.generateId();

        RequestForVaccineProcurement request = new RequestForVaccineProcurement(requestId, reason, comment, count, vaccine, requestType, time);

        requestForVaccineProcurementService.save(request);

        response.sendRedirect(bURL + "requests");
    }

    @PostMapping("/update")
    public void postUpdate(@RequestParam  String id, @RequestParam String reason, @RequestParam Integer count,
                       HttpSession session, HttpServletResponse response) throws IOException {
        RequestForVaccineProcurement request = requestForVaccineProcurementService.findOneById(id);
        if(reason != null && count != null) {
            request.setRequestReason(reason);
            request.setCount(count);
            requestForVaccineProcurementService.update(request);
            response.sendRedirect(bURL + "requests");
        } else {
            response.sendRedirect(bURL + "requests");
        }
    }

    @PostMapping("/return")
    public void postReturn(@RequestParam  String id, @RequestParam String comment,
                           HttpSession session, HttpServletResponse response) throws IOException {
        RequestForVaccineProcurement request = requestForVaccineProcurementService.findOneById(id);
        if(comment != null) {
            requestForVaccineProcurementService.sendBack(id, comment);
        }

        response.sendRedirect(bURL + "requests");
    }

    @PostMapping("/decline")
    public void postDecline(@RequestParam  String id, @RequestParam String comment,
                           HttpSession session, HttpServletResponse response) throws IOException {
        RequestForVaccineProcurement request = requestForVaccineProcurementService.findOneById(id);
        if(comment != null) {
            requestForVaccineProcurementService.decline(id, comment);
        }

        response.sendRedirect(bURL + "requests");
    }

    @PostMapping("/approve")
    public void approve(@RequestParam  String id,
                           HttpSession session, HttpServletResponse response) throws IOException {
        requestForVaccineProcurementService.approve(id);

        response.sendRedirect(bURL + "requests");
    }

}
