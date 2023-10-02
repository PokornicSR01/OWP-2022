package com.ftn.owpProject2022.daoImplementation;

import com.ftn.owpProject2022.dao.ApplicationForVaccinationDAO;
import com.ftn.owpProject2022.dao.UserDAO;
import com.ftn.owpProject2022.enums.Role;
import com.ftn.owpProject2022.model.ApplicationForVaccination;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.ApplicationForVaccinationService;
import com.ftn.owpProject2022.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserDAOImplementation implements UserDAO {

    private JdbcTemplate jdbcTemplate;
    private VaccineService vaccineService;
    private ApplicationForVaccinationService applicationService;

    @Autowired
    public UserDAOImplementation(JdbcTemplate jdbcTemplate, VaccineService vaccineService, ApplicationForVaccinationService application){
        this.jdbcTemplate = jdbcTemplate;
        this.vaccineService = vaccineService;
        this.applicationService = application;
    }

    private class UserRowCallBackHandler implements RowCallbackHandler {

        private Map<String, User> users = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            String id = resultSet.getString(index++);
            String firstName = resultSet.getString(index++);
            String lastName = resultSet.getString(index++);
            String email = resultSet.getString(index++);
            String password = resultSet.getString(index++);
            String address = resultSet.getString(index++);
            LocalDate dateOfBirth = LocalDate.parse(resultSet.getString(index++));
            String uniquePersonalIdentificationNumber = resultSet.getString(index++);
            String phoneNumber = resultSet.getString(index++);
            LocalDate registrationDate = LocalDate.parse(resultSet.getString(index++));
            Role role = Role.valueOf(resultSet.getString(index++));
            List<Vaccine> vaccines = vaccineService.findUserRecievedVaccines(id);
            List<ApplicationForVaccination> applications = new ArrayList<>();

            User user = users.get(id);
            if(user == null) {
                user = new User(id, firstName, lastName, email, password, address
                        , dateOfBirth, uniquePersonalIdentificationNumber, phoneNumber,
                        registrationDate, role, vaccines, applications);
                users.put(user.getId(), user);
            }
        }

        public List<User> getUsers() {
            return new ArrayList<>(users.values());
        }

    }

    @Override
    public int save(User user) {
        String sql = "INSERT INTO user (id, first_name, last_name, email, password, address, " +
                "date_of_birth, unique_personal_identification_number, " +
                "phone_number, registration_date, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getAddress(),
                    user.getDateOfBirth(),
                    user.getUniquePersonalIdentificationNumber(),
                    user.getPhoneNumber(),
                    user.getRegistrationDate(),
                    user.getRole().toString()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(User user) {
        String sql = "UPDATE user SET first_name = ?, last_name = ?, email = ?, password = ?, address = ?, " +
                "phone_number = ? WHERE id = ?";

        try {
            boolean success = jdbcTemplate.update(sql,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getAddress(),
                    user.getPhoneNumber(),
                    user.getId()
            ) == 1;

            return success ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public User findOneById(String id) {
        String sql = "SELECT * FROM user WHERE id = ?";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (!rowCallbackHandler.getUsers().isEmpty()) {
            return rowCallbackHandler.getUsers().get(0);
        } else {
            return null;
        }
    }

    @Override
    public User findOneByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM user u WHERE u.email = ? AND u.password = ?";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email, password);

        if (!rowCallbackHandler.getUsers().isEmpty()) {
            return rowCallbackHandler.getUsers().get(0);
        } else {
            return null;
        }
    }
}
