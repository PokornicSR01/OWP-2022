package com.ftn.owpProject2022.service;

import com.ftn.owpProject2022.model.RequestForVaccineProcurement;

import java.util.List;

public interface RequestForVaccineProcurementService {
    RequestForVaccineProcurement findOneById(String id);
    List<RequestForVaccineProcurement> findAllSent();
    List<RequestForVaccineProcurement> findAllReturned();
    List<RequestForVaccineProcurement> findAllRejected();
    RequestForVaccineProcurement save(RequestForVaccineProcurement requestForVaccineProcurement);
    RequestForVaccineProcurement update(RequestForVaccineProcurement requestForVaccineProcurement);
    int approve(String id);
    int decline(String id, String comment);
    int sendBack(String id, String comment);
}
