package com.ftn.owpProject2022.dao;

import com.ftn.owpProject2022.model.NewsAboutPatients;

public interface NewsAboutPatientsDAO {
    NewsAboutPatients getNewsAboutPatients();
    int update(NewsAboutPatients newsAboutPatients);
}
