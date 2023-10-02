package com.ftn.owpProject2022.service;

import com.ftn.owpProject2022.model.NewsAboutPatients;

public interface NewsAboutPatientsService {
    NewsAboutPatients getNewsAboutPatients();
    NewsAboutPatients update(NewsAboutPatients newsAboutPatients);
}
