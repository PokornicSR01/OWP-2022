package com.ftn.owpProject2022.serviceImplementation;

import com.ftn.owpProject2022.dao.NewsAboutPatientsDAO;
import com.ftn.owpProject2022.model.NewsAboutPatients;
import com.ftn.owpProject2022.service.NewsAboutPatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NewsAboutPatientsServiceImplementation implements NewsAboutPatientsService {
    @Autowired
    private NewsAboutPatientsDAO newsAboutPatientsDAO;

    @Override
    public NewsAboutPatients getNewsAboutPatients() {
        return newsAboutPatientsDAO.getNewsAboutPatients();
    }

    @Override
    public NewsAboutPatients update(NewsAboutPatients newsAboutPatients) {
        newsAboutPatientsDAO.update(newsAboutPatients);
        return newsAboutPatients;
    }
}
