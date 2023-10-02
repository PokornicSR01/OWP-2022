package com.ftn.owpProject2022.dao;

import com.ftn.owpProject2022.model.News;

import java.util.List;

public interface NewsDAO {
    List<News> findAll();
    List<News> findTodaysNews();
    int save(News news);
}
