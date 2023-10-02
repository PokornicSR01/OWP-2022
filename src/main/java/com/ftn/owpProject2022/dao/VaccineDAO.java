package com.ftn.owpProject2022.dao;

import com.ftn.owpProject2022.model.Vaccine;

import java.util.List;

public interface VaccineDAO {
    Vaccine findOneById(String id);
    List<Vaccine> findAll();
    List<Vaccine> findAllAvaibleVaccines();
    List<Vaccine> findUserRecievedVaccines(String userId);
    int save(Vaccine vaccine);
    int update(Vaccine vaccine);
    int delete(String id);
    List<Vaccine> sortByName(String type);
    List<Vaccine> sortByManufacturer(String type);
    List<Vaccine> sortByCountry(String type);
    List<Vaccine> sortByQuantity(String type);
    List<Vaccine> searchVaccine(String name, String manufacturer, String country, Integer minQuantity, Integer maxQuantity);
}
