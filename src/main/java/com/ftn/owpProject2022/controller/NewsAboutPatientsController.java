package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.model.NewsAboutPatients;
import com.ftn.owpProject2022.service.NewsAboutPatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping(value = "newsAboutPatients")
public class NewsAboutPatientsController implements ServletContextAware {

    private  String bURL;
    private ServletContext servletContext;
    private NewsAboutPatientsService newsAboutPatientsService;


    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath()+"/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public void NewsAboutPatientsController(ServletContext servletContext, NewsAboutPatientsService newsAboutPatientsService){
        this.servletContext = servletContext;
        this.newsAboutPatientsService = newsAboutPatientsService;
    }

    @PostMapping("/update")
    public void update(@RequestParam Integer numberOfPatientsOnVentilators, @RequestParam Integer numberOfTestedLast24hours,
                       @RequestParam Integer numberOfHospitalized, @RequestParam Integer numberOfInfectedLast24hours,
                       HttpServletResponse response) throws IOException {
        NewsAboutPatients newsAboutPatients = newsAboutPatientsService.getNewsAboutPatients();
        if(numberOfPatientsOnVentilators != null){
            newsAboutPatients.setNumberOfPatientsOnVentilators(numberOfPatientsOnVentilators);
        }
        if(numberOfTestedLast24hours != null) {
            newsAboutPatients.setNumberOfTestedLast24hours(numberOfTestedLast24hours);
        }
        if(numberOfHospitalized != null) {
            newsAboutPatients.setNumberOfHospitalized(numberOfHospitalized);
        }
        if(numberOfInfectedLast24hours != null) {
            newsAboutPatients.setNumberOfInfectedLast24hours(numberOfInfectedLast24hours);
            int newTotalNumberOfInfected = newsAboutPatients.getTotalNumberOfInfected() + numberOfInfectedLast24hours;
            newsAboutPatients.setTotalNumberOfInfected(newTotalNumberOfInfected);
        }
        newsAboutPatients.setPublicationDate(LocalDate.now());
        newsAboutPatientsService.update(newsAboutPatients);
        response.sendRedirect(bURL + "news");
    }

}
