package com.ftn.owpProject2022.daoImplementation;

import com.ftn.owpProject2022.dao.VaccineDAO;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.model.VaccineMaker;
import com.ftn.owpProject2022.service.VaccineMakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class VaccineDAOImplementation implements VaccineDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VaccineMakerService vaccineMakerService;

    private class VaccineRowCallBackHandler implements RowCallbackHandler {

        private Map<String, Vaccine> vaccines = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            String id = resultSet.getString(index++);
            String name = resultSet.getString(index++);
            int availableQuantity = resultSet.getInt(index++);
            String makerId = resultSet.getString(index++);
            VaccineMaker vaccineMaker = vaccineMakerService.findOneById(makerId);

            Vaccine vaccine = vaccines.get(id);
            if(vaccine == null) {
                vaccine = new Vaccine(id, name, availableQuantity, vaccineMaker);
                vaccines.put(vaccine.getId(), vaccine);
            }
        }

        public List<Vaccine> getVaccines() {
            return new ArrayList<>(vaccines.values());
        }

    }

    @Override
    public int save(Vaccine vaccine) {
        String sql = "INSERT INTO vaccine (id, name, available_quantity, vaccine_maker_id) " +
                "VALUES (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    vaccine.getId(),
                    vaccine.getName(),
                    vaccine.getAvailableQuantity(),
                    vaccine.getVaccineMaker().getId()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Vaccine vaccine) {
        String sql = "UPDATE vaccine SET name = ?, vaccine_maker_id = ?, available_quantity = ? " +
                "WHERE id = ?";

        try {
            jdbcTemplate.update(sql,
                    vaccine.getName(),
                    vaccine.getVaccineMaker().getId(),
                    vaccine.getAvailableQuantity(),
                    vaccine.getId()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        String sql = "DELETE FROM vaccine WHERE id = ?";
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
    public List<Vaccine> sortByName(String type) {
        String sql = "SELECT * FROM vaccine ";
        String order;
        if(type.equals("ASC")){
            order = "ORDER BY name ASC";
        } else {
            order = "ORDER BY name DESC";
        }
        sql += order;
        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> sortByManufacturer(String type) {
        String sql = "SELECT v.* FROM vaccine v JOIN vaccine_maker vm WHERE v.vaccine_maker_id = vm.id ";
        if(type.equals("ASC")){
            sql += "ORDER BY vm.manufacturer ASC";
        } else {
            sql += "ORDER BY vm.manufacturer DESC";
        }
        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> sortByCountry(String type) {
        String sql = "SELECT v.* FROM vaccine v JOIN vaccine_maker vm WHERE v.vaccine_maker_id = vm.id ";
        if(type.equals("ASC")){
            sql += "ORDER BY vm.production_country ASC";
        } else {
            sql += "ORDER BY vm.production_country DESC";
        }
        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> sortByQuantity(String type) {
        String sql = "SELECT * FROM vaccine ";
        String order;
        if(type.equals("ASC")){
            order = "ORDER BY available_quantity ASC";
        } else {
            order = "ORDER BY available_quantity DESC";
        }
        sql += order;
        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> searchVaccine(String name, String manufacturer, String country, Integer minQuantity, Integer maxQuantity) {
        String sql = "SELECT v.* FROM vaccine v JOIN vaccine_maker vm WHERE v.vaccine_maker_id = vm.id ";

        List<Object> params = new ArrayList<>();

        if (!name.trim().isEmpty()) {
            sql += " AND v.name LIKE ?";
            params.add("%" + name.trim() + "%");
        }
        if (!manufacturer.trim().isEmpty()) {
            sql += " AND vm.manufacturer LIKE ?";
            params.add("%" + manufacturer.trim() + "%");
        }
        if (!country.trim().isEmpty()) {
            sql += " AND vm.production_country LIKE ?";
            params.add("%" + country.trim()+ "%");
        }
        if (minQuantity != null) {
            sql += " AND v.available_quantity >= ?";
            params.add(minQuantity);
        }
        if (maxQuantity != null) {
            sql += " AND v.available_quantity <= ?";
            params.add(maxQuantity);
        }

        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, params.toArray(), rowCallbackHandler);

        return rowCallbackHandler.getVaccines();
    }

    @Override
    public Vaccine findOneById(String id) {
        String sql = "SELECT * FROM vaccine WHERE id = ?";

        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (!rowCallbackHandler.getVaccines().isEmpty()) {
            return rowCallbackHandler.getVaccines().get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Vaccine> findAll() {
        String sql = "SELECT * FROM vaccine";
        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> findAllAvaibleVaccines() {
        String sql = "SELECT * FROM vaccine WHERE available_quantity > 0";
        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> findUserRecievedVaccines(String userId) {
        String sql = "SELECT v.* FROM vaccine v JOIN application_for_vaccination av WHERE av.vaccine_id = v.id AND av.patient_id = ? AND vaccination_completed = true";
        VaccineDAOImplementation.VaccineRowCallBackHandler rowCallbackHandler = new VaccineDAOImplementation.VaccineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, userId);

        return rowCallbackHandler.getVaccines();
    }


}
