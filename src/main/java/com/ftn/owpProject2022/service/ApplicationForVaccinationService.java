package com.ftn.owpProject2022.service;

import com.ftn.owpProject2022.model.ApplicationForVaccination;
import com.ftn.owpProject2022.model.Vaccine;

import java.util.List;
import java.util.Set;

public interface ApplicationForVaccinationService {
    ApplicationForVaccination findOneById(String id);
    List<ApplicationForVaccination> findAll();
    List<ApplicationForVaccination> findUsersApplications(String userID);
    List<ApplicationForVaccination> findUsersFinishedApplications(String userId);
    ApplicationForVaccination save(ApplicationForVaccination applicationForVaccination);
    int deleteUsersApplications(String userId);
    int delete(String id);
    int approve(String applicationId);
    int decline(String applicationId);
    List<ApplicationForVaccination> searchApplication(String firstName, String lastName, String UPIN);
}
