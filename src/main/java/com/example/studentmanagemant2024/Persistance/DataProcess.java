package com.example.studentmanagemant2024.Persistance;

import com.example.studentmanagemant2024.Persistance.impl.Data;
import com.example.studentmanagemant2024.dto.StudentDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

public  final class DataProcess implements Data {
    static String SAVE_STUDENT="INSERT INTO student(id,name,city,age) VALUES(?,?,?,?)";
    static String GET_STUDENT="SELECT * FROM student WHERE id=?";
    static String UPDATE_STUDENT="UPDATE student SET name = ? ,city =?,age=? WHERE id =? ";
    static String DELETE_STUDENT="DELETE FROM student WHERE id=?";
    @Override
    public StudentDto getStudent(String id, Connection connection) {
        var studentdto = new StudentDto();
        try {
            var ps = connection.prepareStatement(GET_STUDENT);
            ps.setString(1, id);
            var resultset = ps.executeQuery();
            while (resultset.next()) {
                studentdto.setId(resultset.getString("id"));
                studentdto.setName(resultset.getString("name"));
                studentdto.setCity(resultset.getString("city"));
                studentdto.setAge(resultset.getString("age"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentdto;
    }

        @Override
    public boolean saveStudent(StudentDto studentDto, Connection connection) {
            try {
                var preparedStatement = connection.prepareStatement(SAVE_STUDENT);
                preparedStatement.setString(1, studentDto.getId());
                preparedStatement.setString(2, studentDto.getName());
                preparedStatement.setString(3, studentDto.getCity());
                preparedStatement.setString(4, studentDto.getAge());
               return preparedStatement.executeUpdate()!=0;


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public boolean updateStudent(String id,StudentDto studentDto, Connection connection) {
        try {
            var preparedStatement = connection.prepareStatement(UPDATE_STUDENT);

            preparedStatement.setString(1, studentDto.getName());
            preparedStatement.setString(2, studentDto.getCity());
            preparedStatement.setString(3, studentDto.getAge());
            preparedStatement.setString(4, id);
            return preparedStatement.executeUpdate()!=0;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public boolean deleteStudent(String id, Connection connection) {

        try {
            var ps=  connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1,id);
            return ps.executeUpdate()!=0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
