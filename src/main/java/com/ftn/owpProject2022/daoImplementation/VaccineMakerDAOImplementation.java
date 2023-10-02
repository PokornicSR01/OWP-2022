package com.ftn.owpProject2022.daoImplementation;

import com.ftn.owpProject2022.dao.VaccineMakerDAO;
import com.ftn.owpProject2022.enums.Role;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.model.VaccineMaker;
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
public class VaccineMakerDAOImplementation implements VaccineMakerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class VaccineMakerRowCallBackHandler implements RowCallbackHandler {

        private Map<String, VaccineMaker> vaccineMakers = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            String id = resultSet.getString(index++);
            String manufacturer = resultSet.getString(index++);
            String productionCountry = resultSet.getString(index++);
            List<Vaccine> vaccines = new ArrayList<>();

            VaccineMaker vaccineMaker = vaccineMakers.get(id);
            if(vaccineMaker == null) {
                vaccineMaker = new VaccineMaker(id, manufacturer, productionCountry, vaccines);
                vaccineMakers.put(vaccineMaker.getId(), vaccineMaker);
            }
        }

        public List<VaccineMaker> getVaccineMakers() {
            return new ArrayList<>(vaccineMakers.values());
        }

    }

    @Override
    public int save(VaccineMaker vaccineMaker) {
        String sql = "INSERT INTO vaccine_maker (id, manufacturer, production_country) " +
                "VALUES (?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    vaccineMaker.getId(),
                    vaccineMaker.getManufacturer(),
                    vaccineMaker.getProductionCountry()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(VaccineMaker vaccineMaker) {
        String sql = "UPDATE vaccine_maker SET manufacturer = ?, production_country = ? " +
                "WHERE id = ?";

        try {
            jdbcTemplate.update(sql,
                    vaccineMaker.getManufacturer(),
                    vaccineMaker.getProductionCountry(),
                    vaccineMaker.getId()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        String sql = "DELETE FROM vaccine_maker WHERE id = ?";
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
    public VaccineMaker findOneById(String id) {
        String sql = "SELECT * FROM vaccine_maker WHERE id = ?";

        VaccineMakerRowCallBackHandler rowCallbackHandler = new VaccineMakerRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (!rowCallbackHandler.getVaccineMakers().isEmpty()) {
            return rowCallbackHandler.getVaccineMakers().get(0);
        } else {
            return null;
        }
    }

    @Override
    public VaccineMaker findOneByVaccine(String vaccineId) {
        String sql = "SELECT vm.* FROM vaccine_maker vm JOIN vaccine v WHERE v.makerId = vm.id AND v.id = ?";
        VaccineMakerRowCallBackHandler rowCallbackHandler = new VaccineMakerRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, vaccineId);

        if (!rowCallbackHandler.getVaccineMakers().isEmpty()) {
            return rowCallbackHandler.getVaccineMakers().get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<VaccineMaker> findAll() {
        String sql = "SELECT * FROM vaccine_maker";
        VaccineMakerRowCallBackHandler rowCallbackHandler = new VaccineMakerRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getVaccineMakers();
    }


}
