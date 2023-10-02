package com.ftn.owpProject2022.service;

import com.ftn.owpProject2022.model.VaccineMaker;

import java.util.List;

public interface VaccineMakerService {
    VaccineMaker findOneById(String id);
    VaccineMaker findOneByVaccine(String vaccineId);
    List<VaccineMaker> findAll();
    VaccineMaker save(VaccineMaker vaccineMaker);
    VaccineMaker update(VaccineMaker vaccineMaker);
    VaccineMaker delete(String id);
}
