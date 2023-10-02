package com.ftn.owpProject2022.service;

import com.ftn.owpProject2022.model.News;

import java.util.List;

public interface NewsService {
    List<News> findAll();
    List<News> findTodaysNews();
    News save(News news);
}
