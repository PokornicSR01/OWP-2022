package com.ftn.owpProject2022.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vaccine {
    private String id;
    private String name;
    private int availableQuantity;
    private VaccineMaker vaccineMaker;
}
