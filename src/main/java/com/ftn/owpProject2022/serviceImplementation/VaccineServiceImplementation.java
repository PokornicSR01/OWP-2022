package com.ftn.owpProject2022.serviceImplementation;

import com.ftn.owpProject2022.dao.VaccineDAO;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.model.VaccineMaker;
import com.ftn.owpProject2022.service.VaccineMakerService;
import com.ftn.owpProject2022.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VaccineServiceImplementation implements VaccineService {
    @Autowired
    private VaccineDAO vaccineDAO;

    @Override
    public Vaccine findOneById(String id) {
        return vaccineDAO.findOneById(id);
    }

    @Override
    public List<Vaccine> findAll() {
        return vaccineDAO.findAll();
    }

    @Override
    public List<Vaccine> findAllAvaibleVaccines() {
        return vaccineDAO.findAllAvaibleVaccines();
    }

    @Override
    public List<Vaccine> findUserRecievedVaccines(String userId) {
        return vaccineDAO.findUserRecievedVaccines(userId);
    }

    @Override
    public Vaccine save(Vaccine vaccine) {
        vaccineDAO.save(vaccine);
        return vaccine;
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        vaccineDAO.update(vaccine);
        return vaccine;
    }

    @Override
    public Vaccine delete(String id) {
        Vaccine deleted = vaccineDAO.findOneById(id);
        vaccineDAO.delete(id);
        return deleted;
    }

    @Override
    public List<Vaccine> sortByName(String type) {
        return vaccineDAO.sortByName(type);
    }

    @Override
    public List<Vaccine> sortByManufacturer(String type) {
        return vaccineDAO.sortByManufacturer(type);
    }

    @Override
    public List<Vaccine> sortByCountry(String type) {
        return vaccineDAO.sortByCountry(type);
    }

    @Override
    public List<Vaccine> sortByQuantity(String type) {
        return vaccineDAO.sortByQuantity(type);
    }

    @Override
    public List<Vaccine> searchVaccine(String name, String manufacturer, String country, Integer minCount, Integer maxCount) {
        return vaccineDAO.searchVaccine(name, manufacturer, country, minCount, maxCount);
    }
}
