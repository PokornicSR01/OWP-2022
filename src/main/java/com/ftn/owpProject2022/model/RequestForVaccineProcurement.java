package com.ftn.owpProject2022.model;

import com.ftn.owpProject2022.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestForVaccineProcurement {
    private String id;
    private String requestReason;
    private String comment;
    private int count;
    private Vaccine vaccine;
    private RequestType requestType;
    private LocalDateTime createdAt;
}
