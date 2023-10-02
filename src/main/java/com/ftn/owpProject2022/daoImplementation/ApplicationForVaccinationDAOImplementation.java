package com.ftn.owpProject2022.daoImplementation;

import com.ftn.owpProject2022.dao.ApplicationForVaccinationDAO;
import com.ftn.owpProject2022.model.ApplicationForVaccination;
import com.ftn.owpProject2022.model.NewsAboutPatients;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.UserService;
import com.ftn.owpProject2022.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class ApplicationForVaccinationDAOImplementation implements ApplicationForVaccinationDAO {

    private JdbcTemplate jdbcTemplate;
    private UserService userService;
    private VaccineService vaccineService;

    @Autowired
    public ApplicationForVaccinationDAOImplementation(JdbcTemplate jdbcTemplate, UserService userService, VaccineService vaccineService){
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
        this.vaccineService = vaccineService;
    }

    private class ApplicationRowCallBackHandler implements RowCallbackHandler {

        private Map<String, ApplicationForVaccination> listOfApplications = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            String id = resultSet.getString(index++);
            String patientId = resultSet.getString(index++);
            String vaccineId = resultSet.getString(index++);
            LocalDateTime appointmentTime = LocalDateTime.parse(resultSet.getString(index++));
            boolean vaccinationCompleted = resultSet.getBoolean(index++);

            User patient = userService.findOneById(patientId);
            Vaccine vaccine = vaccineService.findOneById(vaccineId);

            ApplicationForVaccination application = listOfApplications.get(id);
            if(application == null) {
                application = new ApplicationForVaccination(id, patient, vaccine, appointmentTime, vaccinationCompleted);
                listOfApplications.put(application.getId(), application);
            }
        }

        public List<ApplicationForVaccination> getApplications() {
            return new ArrayList<>(listOfApplications.values());
        }
    }

    @Override
    public ApplicationForVaccination findOneById(String id) {
        String sql = "SELECT * FROM application_for_vaccination WHERE id = ?";

        ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (!rowCallbackHandler.getApplications().isEmpty()) {
            return rowCallbackHandler.getApplications().get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<ApplicationForVaccination> findAll() {
        String sql = "SELECT * FROM application_for_vaccination WHERE vaccination_completed = false";
        ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getApplications();
    }

    @Override
    public List<ApplicationForVaccination> findUsersApplications(String userId) {
        String sql = "SELECT * FROM application_for_vaccination WHERE vaccination_completed = false AND patient_id = ?";
        ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, userId);

        return rowCallbackHandler.getApplications();
    }

    @Override
    public int save(ApplicationForVaccination applicationForVaccination) {
        String sql = "INSERT INTO application_for_vaccination (id, patient_id, vaccine_id, appointment_time, vaccination_completed) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    applicationForVaccination.getId(),
                    applicationForVaccination.getPatient().getId(),
                    applicationForVaccination.getVaccine().getId(),
                    applicationForVaccination.getAppointmentTime().toString(),
                    applicationForVaccination.isVaccinationCompleted()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        String sql = "DELETE FROM application_for_vaccination WHERE id = ? ";
        try {
            boolean success = jdbcTemplate.update(sql,
                    id) == 1;

            return success ? 1 : 0;
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteUsersApplications(String userId) {
        String sql = "DELETE FROM application_for_vaccination WHERE patient_id = ? AND vaccination_completed = false ";
        try {
            boolean success = jdbcTemplate.update(sql,
                    userId) == 1;

            return success ? 1 : 0;
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int approve(String applicationId) {
        String sql = "UPDATE application_for_vaccination SET vaccination_completed = true WHERE id = ?";
        try {
            boolean success = jdbcTemplate.update(sql,
                    applicationId) == 1;

            return success ? 1 : 0;
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int decline(String applicationId) {
        String sql = "DELETE FROM application_for_vaccination WHERE id = ?";
        try {
            boolean success = jdbcTemplate.update(sql,
                    applicationId) == 1;

            return success ? 1 : 0;
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<ApplicationForVaccination> searchApplication(String firstName, String lastName, String UPIN) {
        String sql = "SELECT a.* FROM application_for_vaccination a JOIN user u WHERE a.patient_id = u.id and a.vaccination_completed = false ";

        List<Object> params = new ArrayList<>();

        if (!firstName.trim().isEmpty()) {
            sql += " AND u.first_name LIKE ?";
            params.add("%" + firstName.trim() + "%");
        }
        if (!lastName.trim().isEmpty()) {
            sql += " AND u.last_name LIKE ?";
            params.add("%" + lastName.trim() + "%");
        }
        if (!UPIN.trim().isEmpty()) {
            sql += " AND u.unique_personal_identification_number LIKE ?";
            params.add("%" + UPIN.trim() + "%");
        }

        ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler();
        jdbcTemplate.query(sql, params.toArray(), rowCallbackHandler);

        return rowCallbackHandler.getApplications();
    }

    @Override
    public List<ApplicationForVaccination> findUsersFinishedApplications(String userId) {
        String sql = "SELECT * FROM application_for_vaccination WHERE vaccination_completed = true AND patient_id = ?";
        ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationForVaccinationDAOImplementation.ApplicationRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, userId);

        return rowCallbackHandler.getApplications();
    }

}
