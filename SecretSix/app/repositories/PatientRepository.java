package repositories;

import model.PatientEntity;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 3/19/2015.
 */
public class PatientRepository {
    public static final String TABLE_Patient = "Patient";
    private String[] allColumns = {
            SsSqLiteHelper.COLUMN_PATIENTID,
            SsSqLiteHelper.COLUMN_PATIENTFIRSTNAME,
            SsSqLiteHelper.COLUMN_PATIENTLASTNAME,
            SsSqLiteHelper.COLUMN_PATIENTTYPE,
    };

    Connection connection = null;
    public ArrayList<PatientEntity> GetPatients()
    {
        PatientEntity patient = null;
        ArrayList<PatientEntity> patients = new ArrayList<PatientEntity>();
        Connection connection = null;
        try
        {
            // create a database connection

            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_Patient );

            while(rs.next()) {
                patient = resultToPatient(rs);
                patients.add(patient);
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
        return patients;
    }

    public PatientEntity GetPatient(String patientId)
    {
        PatientEntity patient = null;
        Connection connection = null;
        try
        {
            // create a database connection

            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_Patient + " where " + SsSqLiteHelper.COLUMN_PATIENTID + " = '" + patientId + "'");
            patient = resultToPatient(rs);
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
        return patient;
    }

    public boolean SavePatient(PatientEntity patient)
    {
        Connection connection = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = connection.createStatement();
            String sql = "UPDATE " + TABLE_Patient + " set ";
            sql += SsSqLiteHelper.COLUMN_PATIENTFIRSTNAME + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_PATIENTLASTNAME + " = ?, ";
            sql += SsSqLiteHelper.COLUMN_PATIENTTYPE + " = ? ";
            sql += " where " + SsSqLiteHelper.COLUMN_PATIENTID + " = ?";

            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, patient.getFirstName());
            preparedStmt.setString(2, patient.getLastName());
            preparedStmt.setInt(3, patient.getType());
            preparedStmt.setString(4, patient.getPatientId());

            preparedStmt.executeUpdate();
            connection.commit();
            stmt.close();
            connection.close();
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }

        return true;
    }

    public boolean CreatePatient(PatientEntity patient)
    {
        Connection connection = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String sql = " insert into " + TABLE_Patient + " (" + SsSqLiteHelper.COLUMN_PATIENTID + "," + SsSqLiteHelper.COLUMN_PATIENTFIRSTNAME
                    + "," + SsSqLiteHelper.COLUMN_PATIENTLASTNAME
                    + "," + SsSqLiteHelper.COLUMN_PATIENTTYPE + ")"
                    + " values (?, ?, ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, patient.getPatientId());
            preparedStmt.setString(2, patient.getFirstName());
            preparedStmt.setString(3, patient.getLastName());
            preparedStmt.setInt(4, patient.getType());
            //preparedStmt.setDate   (3, startDate);
            //preparedStmt.setBoolean(4, false);
            //preparedStmt.setInt    (5, 5000);
            try{
                preparedStmt.execute();
                connection.commit();
                preparedStmt.close();
                connection.close();
            }catch(SQLException e1){
                preparedStmt.close();
                connection.close();
                e1.printStackTrace();
                return false;
            }
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }

        return true;
    }

    private PatientEntity resultToPatient(ResultSet rs) throws SQLException {
        PatientEntity patient = new PatientEntity();

        patient.setPatientId(rs.getString(1));
        patient.setFirstName(rs.getString(2));
        patient.setLastName(rs.getString(3));
        patient.setType(rs.getInt(4));
        return patient;
    }
}
