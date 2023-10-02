package com.ftn.owpProject2022.dao;

import com.ftn.owpProject2022.model.ApplicationForVaccination;
import com.ftn.owpProject2022.model.Vaccine;

import java.util.List;

public interface ApplicationForVaccinationDAO {
    ApplicationForVaccination findOneById(String id);
    List<ApplicationForVaccination> findAll();
    List<ApplicationForVaccination> findUsersApplications(String userId);
    int save(ApplicationForVaccination applicationForVaccination);
    int delete(String id);
    int deleteUsersApplications(String userId);
    int approve(String applicationId);
    int decline(String applicationId);
    List<ApplicationForVaccination> searchApplication(String firstName, String lastName, String UPIN);
    List<ApplicationForVaccination> findUsersFinishedApplications(String userId);

}
