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

    public ArrayList<TodoEntity> GetTodos(boolean includeAll)
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

            String sql;
            sql = "SELECT DISTINCT STUDY.studyid, PATIENT.patientid, PATIENT.FirstName, PATIENT.lastName, STUDY.ObservationCodes, obs.dateobserved as lastDateObserved "
                    + ", CAST (((julianday('now') - julianday(obs.dateObserved)) - STUDY.frequency) AS INTEGER) "
                    + ", CASE WHEN ((julianday('now') - julianday(obs.dateObserved)) - STUDY.frequency) IS NULL THEN 1 "
                    + "       WHEN ((julianday('now') - julianday(obs.dateObserved)) - STUDY.frequency) > 0 THEN 1 "
                    + "ELSE 0 END "
                    + "FROM PATIENT "
                    + "JOIN STUDY ON PATIENT.studyId = STUDY.studyId "
                    + "LEFT OUTER JOIN (SELECT subject, max(dateobserved) as dateObserved "
                    + "FROM OBSERVATION "
                    + "GROUP BY subject) as obs ON PATIENT.PatientId = obs.subject ";

            if (!includeAll)
            {
                sql += "WHERE (((julianday('now') - julianday(obs.dateObserved)) - STUDY.frequency) IS NULL) ";
                sql += "OR (NOT ((julianday('now') - julianday(obs.dateObserved)) < STUDY.frequency)) ";
            }

            sql += "ORDER BY lastDateObserved ASC ";
            ResultSet rs = statement.executeQuery(sql);

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

        todo.setStudyId(rs.getInt(1));
        todo.setSubject(rs.getString(2));
        todo.setSubjectFirstName(rs.getString(3));
        todo.setSubjectLastName(rs.getString(4));
        todo.setObservationCodes(rs.getString(5));
        todo.setLastDateObserved(rs.getString(6));
        todo.setPastDueDays(rs.getInt(7));
        todo.setIsPast(rs.getInt(8));

        return todo;
    }
}
