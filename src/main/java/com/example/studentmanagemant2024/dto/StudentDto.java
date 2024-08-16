package com.example.studentmanagemant2024.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class StudentDto implements Serializable {
    private String id;
    private String name;
    private String city;
    private  String age;
}
