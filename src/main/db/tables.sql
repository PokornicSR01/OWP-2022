drop schema if exists eUpravaOwpProject;
create schema eUpravaOwpProject default  character set utf8;
use eUpravaOwpProject;

CREATE TABLE user (
                      id VARCHAR(50) NOT NULL,
                      first_name VARCHAR(50) NOT NULL,
                      last_name VARCHAR(50) NOT NULL,
                      email VARCHAR(50) NOT NULL,
                      password VARCHAR(50) NOT NULL,
                      address VARCHAR(50),
                      date_of_birth VARCHAR(15),
                      unique_personal_identification_number VARCHAR(13) NOT NULL,
                      phone_number VARCHAR(25),
                      registration_date VARCHAR(15),
                      role ENUM('PATIENT', 'MEDICAL_STAFF', 'ADMINISTRATOR') NOT NULL
);

CREATE TABLE vaccine_maker (
                               id VARCHAR(50) NOT NULL,
                               manufacturer VARCHAR(50) NOT NULL,
                               production_country VARCHAR(50) NOT NULL
);

CREATE TABLE vaccine (
                         id VARCHAR(50) NOT NULL,
                         name VARCHAR(50) NOT NULL,
                         available_quantity INT NOT NULL,
                         vaccine_maker_id VARCHAR(50) NOT NULL
);

CREATE TABLE news (
                      id VARCHAR(50) NOT NULL,
                      title VARCHAR(50) NOT NULL,
                      content VARCHAR(100) NOT NULL,
                      publication_date VARCHAR(20) NOT NULL
);

CREATE TABLE news_about_patients (
                                     id VARCHAR(50) NOT NULL,
                                     number_of_patients_on_ventilators INT NOT NULL,
                                     number_of_tested_last_24_hours INT NOT NULL,
                                     total_number_of_infected INT NOT NULL,
                                     number_of_hospitalized INT NOT NULL,
                                     number_of_infected_last_24_hours INT NOT NULL,
                                     publication_date VARCHAR(20) NOT NULL
);

INSERT INTO news_about_patients(id ,
                                number_of_patients_on_ventilators ,
                                number_of_tested_last_24_hours ,
                                total_number_of_infected ,
                                number_of_hospitalized ,
                                number_of_infected_last_24_hours ,
                                publication_date

)
values(1,0,0,0,0,0,"2023-08-21");

CREATE TABLE application_for_vaccination (
                                             id VARCHAR(50) NOT NULL,
                                             patient_id VARCHAR(50) NOT NULL,
                                             vaccine_id VARCHAR(50) NOT NULL,
                                             appointment_time VARCHAR(20) NOT NULL,
                                             vaccination_completed Boolean
);

CREATE TABLE request_for_vaccine_procurement (
                                                 id VARCHAR(50) NOT NULL,
                                                 request_reason VARCHAR(100) NOT NULL,
                                                 comment VARCHAR(100),
                                                 count INT NOT NULL,
                                                 vaccine_id VARCHAR(50) NOT NULL,
                                                 request_type ENUM('SENT', 'APPROVED', 'REJECTED', 'RETURN_TO_REVISION') NOT NULL,
                                                 created_at VARCHAR(40) NOT NULL
);

INSERT INTO user (id, first_name, last_name, email, password, address,
                  date_of_birth, unique_personal_identification_number,
                  phone_number, registration_date, role)
VALUES ('a', 'admin', 'admin', 'admin@gmail.com', 'admin', 'admin address', '1999-08-15', '1111111111111', '064111111', '2023-08-18', 'ADMINISTRATOR'),
       ('s', 'staff', 'staff', 'staff@gmail.com', 'staff', 'staff address', '2001-11-15', '1234567899999', '065111111', '2023-08-18', 'MEDICAL_STAFF');