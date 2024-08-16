package com.example.studentmanagemant2024.Persistance.impl;

import com.example.studentmanagemant2024.dto.StudentDto;

import java.sql.Connection;

public interface Data {
    StudentDto getStudent(String id, java.sql.Connection connection);
    boolean saveStudent(StudentDto studentDto, java.sql.Connection connection);
    boolean updateStudent(String id,StudentDto studentDto, Connection connection);

    boolean deleteStudent(String id,Connection connection);
}
