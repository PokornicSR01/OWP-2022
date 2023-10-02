package com.ftn.owpProject2022.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationForVaccination {
    private String id;
    private User patient;
    private Vaccine vaccine;
    private LocalDateTime appointmentTime;
    private boolean vaccinationCompleted;
}
