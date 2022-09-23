package uz.pdp.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubjectDao {

    private final JdbcTemplate jdbcTemplate;


    public List<Subject> getAllSubjectForSelect() {
        String sql = "select s.id, s.subject_name\n" +
                "from subject s;";
        return jdbcTemplate.query(sql, (rs, row) ->
                Subject.builder()
                        .id(rs.getInt(1))
                        .subject_name(rs.getString(2))
                        .build()
        );
    }


    public void saveSubject(Subject subject) {


        //add category
        String sql = "insert into subject (subject_name) values (?)";

        jdbcTemplate.update(sql,subject.getSubject_name());

    }





    public Subject getSubject(int id) {
        String sql = "select * from subject where id="+id;
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Subject.class));
    }

    public void updatePosition(Subject subject) {

        String sql = "update subject set subject_name=? where id=?";

        jdbcTemplate.update(sql,subject.getSubject_name(),subject.getId());
    }



    public void delete(int id) {
        String sql = "delete from subject where id=?";
        jdbcTemplate.update(sql,id);

    }


    public  int getCountOfSubjects() {
        try (Connection connection = getConnection()) {

            String sql = "select count(*) fromsubject";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Subject> getAllSubjects(int size, int page){

        List<Subject> subjectList = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from subject limit ? offset (? - 1) * ?");
            preparedStatement.setInt(1,size);
            preparedStatement.setInt(2,page);
            preparedStatement.setInt(3,size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Subject subject = Subject.builder()
                        .id(resultSet.getInt(1))
                        .subject_name(resultSet.getString(2))
                        .build();

                subjectList.add(subject);
            }

            connection.close();




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return subjectList;
    }


    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String username = "postgres";
    static String password = "root123";
    public static Connection getConnection(){

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
