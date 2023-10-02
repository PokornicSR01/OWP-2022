package com.ftn.owpProject2022.daoImplementation;

import com.ftn.owpProject2022.dao.RequestForVaccineProcurementDAO;
import com.ftn.owpProject2022.enums.RequestType;
import com.ftn.owpProject2022.enums.Role;
import com.ftn.owpProject2022.model.RequestForVaccineProcurement;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RequestForVaccineProcurementDAOImplementation implements RequestForVaccineProcurementDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VaccineService vaccineService;

    private class RequestRowCallBackHandler implements RowCallbackHandler {

        private Map<String, RequestForVaccineProcurement> requests = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            String id = resultSet.getString(index++);
            String requestReason = resultSet.getString(index++);
            String comment = resultSet.getString(index++);
            int count = resultSet.getInt(index++);
            String vaccineId = resultSet.getString(index++);
            RequestType requestType = RequestType.valueOf(resultSet.getString(index++));
            LocalDateTime date = LocalDateTime.parse(resultSet.getString(index++));

            Vaccine vaccine = vaccineService.findOneById(vaccineId);

            RequestForVaccineProcurement request = requests.get(id);
            if(request == null) {
                request = new RequestForVaccineProcurement(id, requestReason, comment,
                        count, vaccine, requestType, date);
                requests.put(request.getId(), request);
            }
        }

        public List<RequestForVaccineProcurement> getRequests() {
            return new ArrayList<>(requests.values());
        }

    }

    @Override
    public RequestForVaccineProcurement findOneById(String id) {
        String sql = "SELECT * FROM request_for_vaccine_procurement WHERE id = ?";

        RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler rowCallbackHandler = new RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (!rowCallbackHandler.getRequests().isEmpty()) {
            return rowCallbackHandler.getRequests().get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<RequestForVaccineProcurement> findAllSent() {
        String sql = "SELECT * FROM request_for_vaccine_procurement WHERE request_type = 'SENT'";

        RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler rowCallbackHandler = new RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getRequests();
    }

    @Override
    public List<RequestForVaccineProcurement> findAllReturned() {
        String sql = "SELECT * FROM request_for_vaccine_procurement WHERE request_type = 'RETURN_TO_REVISION'";

        RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler rowCallbackHandler = new RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getRequests();
    }

    @Override
    public List<RequestForVaccineProcurement> findAllRejected() {
        String sql = "SELECT * FROM request_for_vaccine_procurement WHERE request_type = 'REJECTED'";

        RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler rowCallbackHandler = new RequestForVaccineProcurementDAOImplementation.RequestRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getRequests();
    }

    @Override
    public int save(RequestForVaccineProcurement requestForVaccineProcurement) {
        String sql = "INSERT INTO request_for_vaccine_procurement (id, request_reason, comment, count," +
                "vaccine_id, request_type, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    requestForVaccineProcurement.getId(),
                    requestForVaccineProcurement.getRequestReason(),
                    requestForVaccineProcurement.getComment(),
                    requestForVaccineProcurement.getCount(),
                    requestForVaccineProcurement.getVaccine().getId(),
                    requestForVaccineProcurement.getRequestType().toString(),
                    requestForVaccineProcurement.getCreatedAt().toString()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(RequestForVaccineProcurement requestForVaccineProcurement) {
        String sql = "UPDATE request_for_vaccine_procurement SET request_reason = ?, count = ?,request_type = 'SENT'" +
                "WHERE id = ?";

        try {
            jdbcTemplate.update(sql,
                    requestForVaccineProcurement.getRequestReason(),
                    requestForVaccineProcurement.getCount(),
                    requestForVaccineProcurement.getId()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int approve(String id) {
        String sql = "UPDATE request_for_vaccine_procurement SET request_type = 'APPROVED' " +
                "WHERE id = ?";

        try {
            jdbcTemplate.update(sql,
                    id
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int decline(String id, String comment) {
        String sql = "UPDATE request_for_vaccine_procurement SET request_type = 'REJECTED' , comment = ? " +
                "WHERE id = ?";

        try {
            jdbcTemplate.update(sql,
                    comment,
                    id
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int sendBack(String id, String comment) {
        String sql = "UPDATE request_for_vaccine_procurement SET request_type = 'RETURN_TO_REVISION', comment = ? " +
                "WHERE id = ?";

        try {
            jdbcTemplate.update(sql,
                    comment,
                    id
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
