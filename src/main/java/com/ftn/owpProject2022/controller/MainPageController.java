package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.model.News;
import com.ftn.owpProject2022.model.NewsAboutPatients;
import com.ftn.owpProject2022.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping()
public class MainPageController implements ServletContextAware {
    private  String bURL;
    private ServletContext servletContext;
    private NewsAboutPatientsService newsAboutPatientsService;
    private NewsService newsService;

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath()+"/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public MainPageController(ServletContext servletContext, NewsAboutPatientsService newsAboutPatientsService,
                              NewsService newsService){
        this.servletContext = servletContext;
        this.newsService = newsService;
        this.newsAboutPatientsService =newsAboutPatientsService;
    }

    @GetMapping()
    public ModelAndView index(){
        List<News> listOfNews = newsService.findTodaysNews();
        NewsAboutPatients newsAboutPatients = newsAboutPatientsService.getNewsAboutPatients();
        LocalDate todaysDate = LocalDate.now();
        boolean todaysNews = todaysDate.isEqual(newsAboutPatients.getPublicationDate());

        System.out.println(todaysNews);
        System.out.println(todaysDate);
        System.out.println(newsAboutPatients.getPublicationDate());

        ModelAndView modelAndView = new ModelAndView("mainTemplates/index");
        modelAndView.addObject("listOfNews", listOfNews);
        modelAndView.addObject("newsAboutPatients", newsAboutPatients);
        modelAndView.addObject("todaysNews", todaysNews);

        return modelAndView;
    }
}
