package com.ftn.owpProject2022.serviceImplementation;

import com.ftn.owpProject2022.dao.NewsDAO;
import com.ftn.owpProject2022.enums.Role;
import com.ftn.owpProject2022.model.News;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsServiceImplementation implements NewsService {
    @Autowired
    private NewsDAO newsDAO;

    @Override
    public List<News> findTodaysNews() {
        return newsDAO.findTodaysNews();
    }

    @Override
    public News save(News news) {
        newsDAO.save(news);
        return news;
    }

    @Override
    public List<News> findAll() {
        return newsDAO.findAll();
    }
}
