package com.ftn.owpProject2022.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewsAboutPatients {
    private String id;
    private int numberOfPatientsOnVentilators;
    private int numberOfTestedLast24hours;
    private int totalNumberOfInfected;
    private int numberOfHospitalized;
    private int numberOfInfectedLast24hours;
    private LocalDate publicationDate;
}