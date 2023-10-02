package com.ftn.owpProject2022.serviceImplementation;

import com.ftn.owpProject2022.dao.VaccineMakerDAO;
import com.ftn.owpProject2022.model.VaccineMaker;
import com.ftn.owpProject2022.service.VaccineMakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccineMakerServiceImplementation implements VaccineMakerService {
    @Autowired
    private VaccineMakerDAO vaccineMakerDAO;

    @Override
    public VaccineMaker findOneById(String id) {
        return vaccineMakerDAO.findOneById(id);
    }

    @Override
    public VaccineMaker findOneByVaccine(String vaccineId) {
        return vaccineMakerDAO.findOneByVaccine(vaccineId);
    }

    @Override
    public List<VaccineMaker> findAll() {
        return vaccineMakerDAO.findAll();
    }

    @Override
    public VaccineMaker save(VaccineMaker vaccineMaker) {
        vaccineMakerDAO.save(vaccineMaker);
        return vaccineMaker;
    }

    @Override
    public VaccineMaker update(VaccineMaker vaccineMaker) {
        vaccineMakerDAO.update(vaccineMaker);
        return vaccineMaker;
    }

    @Override
    public VaccineMaker delete(String id) {
        VaccineMaker deleted = vaccineMakerDAO.findOneById(id);
        vaccineMakerDAO.delete(id);
        return deleted;
    }
}
