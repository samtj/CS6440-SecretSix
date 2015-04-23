package repositories;

import model.ObservationEntity;
import model.PatientEntity;
import model.StudyEntity;
import model.UserEntity;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 4/7/2015.
 */
public class StudyRepository {
    public static final String TABLE_Study = "Study";
    private String[] allColumns = {
            SsSqLiteHelper.COLUMN_STUDYID,
            SsSqLiteHelper.COLUMN_DESCRIPTION,
            SsSqLiteHelper.COLUMN_ASSIGNEDTO,
            SsSqLiteHelper.COLUMN_OBSERVATIONCODES,
            SsSqLiteHelper.COLUMN_FREQUENCY,
            SsSqLiteHelper.COLUMN_ACTIVE,
            SsSqLiteHelper.COLUMN_STATUS,
            SsSqLiteHelper.COLUMN_NOTE,
    };
    Connection connection = null;

    public StudyEntity GetStudy(int studyId)
    {
        Connection connection = null;
        StudyEntity study = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_Study + " where " + SsSqLiteHelper.COLUMN_STUDYID + " = " + studyId);
            study = resultToStudy(rs);
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
        return study;
    }

    public boolean SaveStudy(StudyEntity study)
    {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = connection.createStatement();
            String sql = "UPDATE " + TABLE_Study + " set ";
            sql += SsSqLiteHelper.COLUMN_DESCRIPTION + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_ASSIGNEDTO + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_OBSERVATIONCODES + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_FREQUENCY + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_ACTIVE + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_STATUS + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_NOTE + " = ? ";
            sql += " where " + SsSqLiteHelper.COLUMN_STUDYID + " = ?";

            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, study.getDescription());
            preparedStmt.setInt(2, study.getAssignedTo());
            preparedStmt.setString(3, study.getObservationCodes());
            preparedStmt.setInt(4, study.getFrequency());
            preparedStmt.setInt(5, study.getActive());
            preparedStmt.setInt(6, study.getStatus());
            preparedStmt.setString(7, study.getNote());
            preparedStmt.setInt(8, study.getStudyId());

            preparedStmt.executeUpdate();
            connection.commit();
            stmt.close();
            connection.close();
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        finally
        {
            try
            {
                if(preparedStmt != null)
                    preparedStmt.close();
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }
        return true;
    }

    public boolean CreateStudy(StudyEntity study)
    {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            int newId = getNewIdentity(connection);

            String sql = " insert into " + TABLE_Study + " (" + SsSqLiteHelper.COLUMN_STUDYID
                    + "," + SsSqLiteHelper.COLUMN_DESCRIPTION
                    + "," + SsSqLiteHelper.COLUMN_ASSIGNEDTO
                    + "," + SsSqLiteHelper.COLUMN_OBSERVATIONCODES
                    + "," + SsSqLiteHelper.COLUMN_FREQUENCY
                    + "," + SsSqLiteHelper.COLUMN_ACTIVE
                    + "," + SsSqLiteHelper.COLUMN_STATUS
                    + "," + SsSqLiteHelper.COLUMN_NOTE
                    + ")"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setInt(1, newId);
            preparedStmt.setString(2, study.getDescription());
            preparedStmt.setInt(3, study.getAssignedTo());
            preparedStmt.setString(4, study.getObservationCodes());
            preparedStmt.setInt(5, study.getFrequency());
            preparedStmt.setInt(6, study.getActive());
            preparedStmt.setInt(7, study.getStatus());
            preparedStmt.setString(8, study.getNote());

            preparedStmt.execute();
            connection.commit();
            preparedStmt.close();
            connection.close();
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        finally
        {
            try
            {
                if(preparedStmt != null)
                    preparedStmt.close();
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }
        return true;
    }

    public ArrayList<StudyEntity> GetStudies()
    {
        StudyEntity study = null;
        ArrayList<StudyEntity> studies = new ArrayList<StudyEntity>();
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_Study );

            while(rs.next()) {
                study = resultToStudy(rs);
                studies.add(study);
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
        return studies;
    }

    public ArrayList<StudyEntity> GetReportStudies()
    {
        StudyEntity study = null;
        ArrayList<StudyEntity> studies = new ArrayList<StudyEntity>();
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT s.studyId, s.Description, count(*) " +
                    "FROM STUDY s " +
                    "JOIN PATIENT p ON s.studyId = p.studyId " +
                    "GROUP BY s.studyId ");

            while(rs.next()) {
                study = resultToStudyReport(rs);
                studies.add(study);
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
        return studies;
    }

    private int getNewIdentity(Connection connection) throws SQLException {
        int id = 0;

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT max(" + SsSqLiteHelper.COLUMN_STUDYID + ") as maxid FROM " + TABLE_Study);
        if(rs.next()){
            id = rs.getInt("maxid");
        }
        statement.close();
        id++;
        return id;
    }


    private StudyEntity resultToStudy(ResultSet rs) throws SQLException {
        StudyEntity study = new StudyEntity();

        study.setStudyId(rs.getInt(1));
        study.setDescription(rs.getString(2));
        study.setAssignedTo(rs.getInt(3));
        study.setObservationCodes(rs.getString(4));
        study.setFrequency(rs.getInt(5));
        study.setActive(rs.getInt(6));
        study.setStatus(rs.getInt(7));
        study.setNote(rs.getString(8));
        return study;
    }

    private StudyEntity resultToStudyReport(ResultSet rs) throws SQLException {
        StudyEntity study = new StudyEntity();

        study.setStudyId(rs.getInt(1));
        study.setDescription(rs.getString(2));
        study.setPatientCount(rs.getInt(3));
        return study;
    }
}
