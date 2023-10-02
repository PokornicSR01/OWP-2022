package com.ftn.owpProject2022.serviceImplementation;

import com.ftn.owpProject2022.dao.ApplicationForVaccinationDAO;
import com.ftn.owpProject2022.model.ApplicationForVaccination;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.ApplicationForVaccinationService;
import com.ftn.owpProject2022.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationForVaccinationServiceImplementation implements ApplicationForVaccinationService {
    @Autowired
    private ApplicationForVaccinationDAO applicationForVaccinationDAO;
    @Autowired
    private VaccineService vaccineService;

    @Override
    public ApplicationForVaccination findOneById(String id) {
        return applicationForVaccinationDAO.findOneById(id);
    }

    @Override
    public List<ApplicationForVaccination> findAll() {
        return applicationForVaccinationDAO.findAll();
    }

    @Override
    public List<ApplicationForVaccination> findUsersApplications(String userID) {
        return applicationForVaccinationDAO.findUsersApplications(userID);
    }

    @Override
    public List<ApplicationForVaccination> findUsersFinishedApplications(String userId) {
        return applicationForVaccinationDAO.findUsersFinishedApplications(userId);
    }

    @Override
    public ApplicationForVaccination save(ApplicationForVaccination applicationForVaccination) {
        applicationForVaccinationDAO.save(applicationForVaccination);
        return applicationForVaccination;
    }

    @Override
    public int deleteUsersApplications(String userId) {
        return applicationForVaccinationDAO.deleteUsersApplications(userId);
    }

    @Override
    public int delete(String id) {
        return applicationForVaccinationDAO.delete(id);
    }

    @Override
    public int approve(String applicationId) {
        ApplicationForVaccination application = applicationForVaccinationDAO.findOneById(applicationId);
        Vaccine vaccine = application.getVaccine();
        int newQuantity = vaccine.getAvailableQuantity() - 1;
        vaccine.setAvailableQuantity(newQuantity);
        vaccineService.update(vaccine);
        return applicationForVaccinationDAO.approve(applicationId);
    }

    @Override
    public int decline(String applicationId) {
        return applicationForVaccinationDAO.decline(applicationId);
    }

    @Override
    public List<ApplicationForVaccination> searchApplication(String firstName, String lastName, String UPIN) {
        return applicationForVaccinationDAO.searchApplication(firstName, lastName, UPIN);
    }

}
