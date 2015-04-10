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
            sql += SsSqLiteHelper.COLUMN_FREQUENCY + " = ? ";
            sql += SsSqLiteHelper.COLUMN_ACTIVE + " = ? ";
            sql += " where " + SsSqLiteHelper.COLUMN_STUDYID + " = ?";

            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, study.getDescription());
            preparedStmt.setInt(2, study.getAssignedTo());
            preparedStmt.setString(3, study.getObservationCodes());
            preparedStmt.setInt(4, study.getFrequency());
            preparedStmt.setInt(5, study.getActive());
            preparedStmt.setInt(6, study.getStudyId());

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

            String sql = " insert into " + TABLE_Study + " (" + SsSqLiteHelper.COLUMN_STUDYID
                    + "," + SsSqLiteHelper.COLUMN_DESCRIPTION
                    + "," + SsSqLiteHelper.COLUMN_ASSIGNEDTO
                    + "," + SsSqLiteHelper.COLUMN_OBSERVATIONCODES
                    + "," + SsSqLiteHelper.COLUMN_FREQUENCY
                    + "," + SsSqLiteHelper.COLUMN_ACTIVE
                    + ")"
                    + " values (?, ?, ?, ?, ?, ?)";

            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setInt(1, study.getStudyId());
            preparedStmt.setString(2, study.getDescription());
            preparedStmt.setInt(3, study.getAssignedTo());
            preparedStmt.setString(4, study.getObservationCodes());
            preparedStmt.setInt(5, study.getFrequency());
            preparedStmt.setInt(6, study.getActive());

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

    private StudyEntity resultToStudy(ResultSet rs) throws SQLException {
        StudyEntity study = new StudyEntity();

        study.setStudyId(rs.getInt(1));
        study.setDescription(rs.getString(2));
        study.setAssignedTo(rs.getInt(3));
        study.setObservationCodes(rs.getString(4));
        study.setFrequency(rs.getInt(5));
        study.setActive(rs.getInt(6));
        return study;
    }
}
