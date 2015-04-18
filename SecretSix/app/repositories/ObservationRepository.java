package repositories;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.ObservationEntity;
import model.PatientEntity;
import model.StudyEntity;
import play.libs.Json;
import play.mvc.Result;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 3/23/2015.
 */
public class ObservationRepository {
    public static final String TABLE_Observation = "Observation";
    private String[] allColumns = {
            SsSqLiteHelper.COLUMN_OBSID,
            SsSqLiteHelper.COLUMN_EXAMID,
            SsSqLiteHelper.COLUMN_CODE,
            SsSqLiteHelper.COLUMN_DISPLAY,
            SsSqLiteHelper.COLUMN_SYSTEM,
            SsSqLiteHelper.COLUMN_QTY,
            SsSqLiteHelper.COLUMN_UNIT,
            SsSqLiteHelper.COLUMN_COMMENT,
            SsSqLiteHelper.COLUMN_SUBJECT,
            SsSqLiteHelper.COLUMN_OBSDATE,
            SsSqLiteHelper.COLUMN_STATUS,
    };
    Connection connection = null;

    public ObservationEntity GetObservation(String observationId)
    {
        ObservationEntity obs = null;
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_Observation + " where " + SsSqLiteHelper.COLUMN_OBSID + " = '" + observationId + "'");
            obs = resultToObservation(rs);
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
        return obs;
    }

    public boolean SaveObservation(ObservationEntity obs)
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


            String sql = "UPDATE " + TABLE_Observation + " set ";
            sql += SsSqLiteHelper.COLUMN_EXAMID + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_CODE + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_DISPLAY + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_SYSTEM + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_QTY + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_UNIT + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_COMMENT + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_SUBJECT + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_OBSDATE + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_STATUS + " = ? ";
            sql += " where " + SsSqLiteHelper.COLUMN_OBSID + " = ?";

            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setInt(1, obs.getExamId());
            preparedStmt.setString(2, obs.getCode());
            preparedStmt.setString(3, obs.getDisplay());
            preparedStmt.setString(4, obs.getSystem());
            preparedStmt.setInt(5, obs.getQuantity());
            preparedStmt.setString(6, obs.getUnit());
            preparedStmt.setString(7, obs.getComment());
            preparedStmt.setString(8, obs.getSubject());
            preparedStmt.setString(9, obs.getDateObserved());
            preparedStmt.setInt(10, obs.getStatus());
            preparedStmt.setString(11, obs.getObservationId());

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

    public boolean CreateObservation(ObservationEntity obs)
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

            String sql = " insert into " + TABLE_Observation + " (" + SsSqLiteHelper.COLUMN_OBSID
                    + "," + SsSqLiteHelper.COLUMN_EXAMID
                    + "," + SsSqLiteHelper.COLUMN_CODE
                    + "," + SsSqLiteHelper.COLUMN_DISPLAY
                    + "," + SsSqLiteHelper.COLUMN_SYSTEM
                    + "," + SsSqLiteHelper.COLUMN_QTY
                    + "," + SsSqLiteHelper.COLUMN_UNIT
                    + "," + SsSqLiteHelper.COLUMN_COMMENT
                    + "," + SsSqLiteHelper.COLUMN_SUBJECT
                    + "," + SsSqLiteHelper.COLUMN_OBSDATE
                    + "," + SsSqLiteHelper.COLUMN_STATUS
                    + ")"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, obs.getObservationId());
            preparedStmt.setInt(2, obs.getExamId());
            preparedStmt.setString(3, obs.getCode());
            preparedStmt.setString(4, obs.getDisplay());
            preparedStmt.setString(5, obs.getSystem());
            preparedStmt.setInt(6, obs.getQuantity());
            preparedStmt.setString(7, obs.getUnit());
            preparedStmt.setString(8, obs.getComment());
            preparedStmt.setString(9, obs.getSubject());
            preparedStmt.setString(10, obs.getDateObserved());
            preparedStmt.setInt(11, obs.getStatus());

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

    public ArrayList<ObservationEntity> GetObservationsByPatientId(String patientId)
    {
        ObservationEntity observation = null;
        ArrayList<ObservationEntity> observations = new ArrayList<ObservationEntity>();
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_Observation + " where " + SsSqLiteHelper.COLUMN_SUBJECT + " = '" + patientId + "'");

            while(rs.next()) {
                observation = resultToObservation(rs);
                observations.add(observation);
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
        return observations;
    }

    private ObservationEntity resultToObservation(ResultSet rs) throws SQLException {
        ObservationEntity obs = new ObservationEntity();

        obs.setObservationId(rs.getString(1));
        obs.setExamId(rs.getInt(2));
        obs.setCode(rs.getString(3));
        obs.setDisplay(rs.getString(4));
        obs.setSystem(rs.getString(5));
        obs.setQuantity(rs.getInt(6));
        obs.setUnit(rs.getString(7));
        obs.setComment(rs.getString(8));
        obs.setSubject(rs.getString(9));
        obs.setDateObserved(rs.getString(10));
        obs.setStatus(rs.getInt(11));
        return obs;
    }

}
