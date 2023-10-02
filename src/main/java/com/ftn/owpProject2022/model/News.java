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
public class News {
    private String id;
    private String title;
    private String content;
    private LocalDate publicationDate;
}
