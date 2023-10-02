package com.ftn.owpProject2022.controller;

import com.ftn.owpProject2022.model.News;
import com.ftn.owpProject2022.model.NewsAboutPatients;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.service.NewsAboutPatientsService;
import com.ftn.owpProject2022.service.NewsService;
import com.ftn.owpProject2022.service.UserService;
import com.ftn.owpProject2022.utilities.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "news")
public class NewsController implements ServletContextAware {

    private  String bURL;
    private ServletContext servletContext;
    private NewsAboutPatientsService newsAboutPatientsService;
    private NewsService newsService;
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
    public NewsController(ServletContext servletContext, NewsService newsService, NewsAboutPatientsService newsAboutPatientsService, UserService userService){
        this.servletContext = servletContext;
        this.newsService = newsService;
        this.newsAboutPatientsService = newsAboutPatientsService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView index(HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = userService.getLoggedUser(session);
        if(loggedUser != null){
            List<News> listOfNews = newsService.findAll();
            LocalDate todaysDate = LocalDate.now();
            NewsAboutPatients newsAboutPatients = newsAboutPatientsService.getNewsAboutPatients();
            ModelAndView modelAndView = new ModelAndView("newsTemplates/news");
            modelAndView.addObject("listOfNews", listOfNews);
            modelAndView.addObject("newsAboutPatients", newsAboutPatients);
            modelAndView.addObject("todaysDate", todaysDate);
            modelAndView.addObject("loggedUser", loggedUser);

            return modelAndView;
        } else {
            response.sendRedirect(bURL + "notAllowed.html");
            return null;
        }

    }

    @PostMapping(value="/add")
    public void create(@RequestParam String title, @RequestParam String content,
                       HttpServletResponse response) throws IOException {
        String id = IdGenerator.generateId();
        LocalDate todaysDate = LocalDate.now();
        System.out.println(todaysDate);
        News news = new News(id, title, content, todaysDate);
        newsService.save(news);
        response.sendRedirect(bURL+"news");
    }
}
