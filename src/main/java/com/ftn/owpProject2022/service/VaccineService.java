package com.ftn.owpProject2022.service;

import com.ftn.owpProject2022.model.Vaccine;

import java.util.List;

public interface VaccineService {
    Vaccine findOneById(String id);
    List<Vaccine> findAll();
    List<Vaccine> findAllAvaibleVaccines();
    List<Vaccine> findUserRecievedVaccines(String userId);
    Vaccine save(Vaccine vaccine);
    Vaccine update(Vaccine vaccine);
    Vaccine delete(String id);
    List<Vaccine> sortByName(String type);
    List<Vaccine> sortByManufacturer(String type);
    List<Vaccine> sortByCountry(String type);
    List<Vaccine> sortByQuantity(String type);
    List<Vaccine> searchVaccine(String name, String manufacturer, String country, Integer minQuantity, Integer maxQuantity);
}
