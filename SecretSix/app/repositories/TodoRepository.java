package repositories;

import model.StudyEntity;
import model.TodoEntity;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 4/9/2015.
 */
public class TodoRepository {
    public static final String TABLE_Study = "Study";
    public static final String TABLE_Patient = "Patient";
    public static final String TABLE_Observation = "Observation";

    Connection connection = null;

    public ArrayList<TodoEntity> GetTodos()
    {
        TodoEntity todo = null;
        ArrayList<TodoEntity> todos = new ArrayList<TodoEntity>();
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT DISTINCT STUDY.studyid, PATIENT.patientid, PATIENT.FirstName, PATIENT.lastName, STUDY.ObservationCodes, obs.dateobserved as lastDateObserved "
                    + "FROM PATIENT "
                    + "JOIN STUDY ON PATIENT.studyId = STUDY.studyId "
                    + "JOIN (SELECT subject, max(dateobserved) as dateObserved "
                    + "FROM OBSERVATION "
                    + "GROUP BY subject) as obs ON PATIENT.PatientId = obs.subject "
                    + "WHERE NOT ((julianday('now') - julianday(obs.dateObserved)) < STUDY.frequency) "
                    + "ORDER BY lastDateObserved ASC ");

            while(rs.next()) {
                todo = resultToTodo(rs);
                todos.add(todo);
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }
        return todos;
    }

    private TodoEntity resultToTodo(ResultSet rs) throws SQLException {
        TodoEntity todo = new TodoEntity();

        //study.setStudyId(rs.getInt(1));
        //study.setDescription(rs.getString(2));

        return todo;
    }
}
