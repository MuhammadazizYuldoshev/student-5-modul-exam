package uz.pdp.student;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uz.pdp.subject.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static uz.pdp.subject.SubjectDao.getConnection;

@Component
@RequiredArgsConstructor
public class StudentDao {

    private final JdbcTemplate jdbcTemplate;

    public List<Student> getAllStudents(int size, int page){
        List<Student> studentList = new ArrayList<>();
        Connection connection = getConnection();
        String sql = "select s.id,s.name,s.lastname,s.email,s.subject_id,su.subject_name from student s join subject su on s.subject_id=su.id  limit ? offset (?-1) * ?";


        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, page);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String lastname = resultSet.getString(3);
                String email = resultSet.getString(4);
                int subjectId = resultSet.getInt(5);
                String subjectName = resultSet.getString(6);

                studentList.add(
                        Student.builder()
                                .id(id)
                                .name(name)
                                .lastname(lastname)
                                .email(email)
                                .subject(
                                        Subject.builder()
                                                .id(subjectId)
                                                .subject_name(subjectName)
                                                .build()
                                )
                                .build()
                );

            }
            return studentList;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public int getCountOfStudents(){
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from student");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void saveStudent(Student student) {
        String sql = "insert into student (name,lastname,email,subject_id) values(?,?,?,?)";
        jdbcTemplate.update(sql,student.getName(),student.getLastname(),student.getEmail(),student.getSubject_id());
    }

    public Student getStudentById(int id) {

        try (Connection connection = getConnection()) {

            String sql = "select s.id,s.name,s.lastname,s.email,su.id from student s join subject su on su.id = s.subject_id where s.id = ?"; // TODO: 25/08/22 select additional informations...
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = Student.builder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .lastname(resultSet.getString(3))
                        .email(resultSet.getString(4))
                        .subject_id(resultSet.getInt(5))
                        .build();
                return student;
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateStudent(Student student) {
        String sql = "update student set name=?,lastname=?,email=?,subject_id=? where id=?";
        jdbcTemplate.update(sql,student.getName(),student.getLastname(),student.getEmail(),student.getSubject_id(),student.getId());
    }


    public void delete(int id) {
        String sql = "delete from student where id=?";
        jdbcTemplate.update(sql,id);
    }
}


