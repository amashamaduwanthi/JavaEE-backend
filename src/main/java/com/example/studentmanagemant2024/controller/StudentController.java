package com.example.studentmanagemant2024.controller;

import com.example.studentmanagemant2024.Persistance.DataProcess;
import com.example.studentmanagemant2024.Util.UtilProcess;
import com.example.studentmanagemant2024.dto.StudentDto;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(urlPatterns = "")
public class StudentController extends HttpServlet {
    Connection connection;
    static String SAVE_STUDENT="INSERT INTO student(id,name,city,age) VALUES(?,?,?,?)";
    static String GET_STUDENT="SELECT * FROM student WHERE id=?";
    static String UPDATE_STUDENT="UPDATE student SET name = ? ,city =?,age=? WHERE id =? ";
    static String DELETE_STUDENT="DELETE FROM student WHERE id=?";
    @SneakyThrows
    @Override
    public void init() throws ServletException{


           var ctx = new InitialContext();
           DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/stuRegistration");
           this.connection =  pool.getConnection();

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//save student
        if (!req.getContentType().toLowerCase().startsWith("application/json")||req.getContentType()==null){
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
//        String id= UUID.randomUUID().toString();


//       List<StudentDto> studentDtoList = jsonb.fromJson(req.getReader(), new ArrayList<StudentDto>() {
//        }.getClass().getGenericSuperclass());
//       studentDtoList.forEach(System.out::println);




        try (var writer = resp.getWriter()){
//            CREATE A JSON OBJECT
            Jsonb jsonb = JsonbBuilder.create();
//            ASSIGN DTO TO A JSON OBJECT
            StudentDto studentDTO = jsonb.fromJson(req.getReader(), StudentDto.class);
            studentDTO.setId(UtilProcess.generateId());
            var saveData = new DataProcess();
//            writer.write(saveData.saveStudent(studentDTO, connection));
            if (saveData.saveStudent(studentDTO, connection)){
                writer.write("Student saved successfully");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else {
                writer.write("Save student failed");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

            }
        } catch (JsonbException e) {
            throw new RuntimeException(e);
        }


//        process
//        BufferedReader reader = req.getReader();
//      StringBuilder sb= new StringBuilder();
//      var writer=resp.getWriter();
//      reader.lines().forEach(line->sb.append(line+"\n"));
//        System.out.println(sb);
//        writer.write(sb.toString());
//        writer.close();





//        JsonReader reader = Json.createReader(req.getReader());
//        JsonObject jsonObject = reader.readObject();
//        System.out.println(jsonObject.getString("email"));

//        JsonReader reader = Json.createReader(req.getReader());
//        JsonArray jsonValues = reader.readArray();
//        for (int i=0;i<jsonValues.size();i++){
//            JsonObject jsonObject = jsonValues.getJsonObject(i);
//            System.out.println(jsonObject.getString("email"));
//        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //        get student details

      var studentId=  req.getParameter("id");
        var dataProcess = new DataProcess();
        try (var writer=resp.getWriter()){
            var student = dataProcess.getStudent(studentId, connection);
            System.out.println(student);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(student,writer);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        delete student
        var id = req.getParameter("id");
//        try {
//            DataProcess dataProcess = new DataProcess();
//            dataProcess.deleteStudent(id,connection);
//
//
//        }
        try (var writer = resp.getWriter()){
            var studentDataProcess = new DataProcess();
            if(studentDataProcess.deleteStudent(id, connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Delete Failed");
            }
        }
        catch (JsonbException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try (var writer = resp.getWriter()){
            Jsonb jsonb = JsonbBuilder.create();
            StudentDto studentDTO = jsonb.fromJson(req.getReader(), StudentDto.class);
            var updateData = new DataProcess();
//            writer.write(updateData.updateStudent(id,studentDTO,connection));
            if (updateData.updateStudent(id,studentDTO,connection)){
                writer.write("Student update successfully");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else {
                writer.write("update student failed");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

            }
        } catch (JsonbException e) {
            throw new RuntimeException(e);
        }
    }
}
