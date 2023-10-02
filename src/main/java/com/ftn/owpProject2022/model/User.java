package com.ftn.owpProject2022.model;

import com.ftn.owpProject2022.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private LocalDate dateOfBirth;
    private String uniquePersonalIdentificationNumber;
    private String phoneNumber;
    private LocalDate registrationDate;
    private Role role;
    private List<Vaccine> vaccineList;
    private List<ApplicationForVaccination> applicationList;
}
