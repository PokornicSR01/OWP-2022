package com.ftn.owpProject2022.dao;

import com.ftn.owpProject2022.model.VaccineMaker;

import java.util.List;

public interface VaccineMakerDAO {
    VaccineMaker findOneById(String id);
    VaccineMaker findOneByVaccine(String vaccineId);
    List<VaccineMaker> findAll();
    int save(VaccineMaker vaccineMaker);
    int update(VaccineMaker vaccineMaker);
    int delete(String id);
}
