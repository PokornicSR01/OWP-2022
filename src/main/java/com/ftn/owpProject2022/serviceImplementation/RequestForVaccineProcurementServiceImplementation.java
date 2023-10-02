package com.ftn.owpProject2022.serviceImplementation;

import com.ftn.owpProject2022.dao.RequestForVaccineProcurementDAO;
import com.ftn.owpProject2022.model.RequestForVaccineProcurement;
import com.ftn.owpProject2022.model.Vaccine;
import com.ftn.owpProject2022.service.RequestForVaccineProcurementService;
import com.ftn.owpProject2022.service.VaccineMakerService;
import com.ftn.owpProject2022.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestForVaccineProcurementServiceImplementation implements RequestForVaccineProcurementService {
    @Autowired
    private RequestForVaccineProcurementDAO requestForVaccineProcurementDAO;
    @Autowired
    private VaccineMakerService vaccineMakerService;
    @Autowired
    private VaccineService vaccineService;

    @Override
    public RequestForVaccineProcurement findOneById(String id) {
        return requestForVaccineProcurementDAO.findOneById(id);
    }

    @Override
    public List<RequestForVaccineProcurement> findAllSent() {
        return requestForVaccineProcurementDAO.findAllSent();
    }

    @Override
    public List<RequestForVaccineProcurement> findAllReturned() {
        return requestForVaccineProcurementDAO.findAllReturned();
    }

    @Override
    public List<RequestForVaccineProcurement> findAllRejected() {
        return requestForVaccineProcurementDAO.findAllRejected();
    }

    @Override
    public RequestForVaccineProcurement save(RequestForVaccineProcurement requestForVaccineProcurement) {
        requestForVaccineProcurementDAO.save(requestForVaccineProcurement);
        return requestForVaccineProcurement;
    }

    @Override
    public RequestForVaccineProcurement update(RequestForVaccineProcurement requestForVaccineProcurement) {
        requestForVaccineProcurementDAO.update(requestForVaccineProcurement);
        return requestForVaccineProcurement;
    }

    @Override
    public int approve(String id) {
        RequestForVaccineProcurement request = requestForVaccineProcurementDAO.findOneById(id);
        Vaccine vaccine = request.getVaccine();
        int newQuantity = vaccine.getAvailableQuantity() + request.getCount();
        vaccine.setAvailableQuantity(newQuantity);
        vaccineService.update(vaccine);
        return requestForVaccineProcurementDAO.approve(id);
    }

    @Override
    public int decline(String id, String comment) {
        return requestForVaccineProcurementDAO.decline(id, comment);
    }

    @Override
    public int sendBack(String id, String comment) {
        return requestForVaccineProcurementDAO.sendBack(id, comment);
    }
}
