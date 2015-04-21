package repositories;
import model.StudyEntity;
import model.UserEntity;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.sql.*;

/**
 * Created by Samuel_Tjokrosoesilo on 3/10/2015.
 */
public class UserRepository {
    public static final String TABLE_User = "User";
    private String[] allColumns = {
            SsSqLiteHelper.COLUMN_USERID,
            SsSqLiteHelper.COLUMN_USERNAME,
            SsSqLiteHelper.COLUMN_USERTYPE,
            SsSqLiteHelper.COLUMN_PASSWORD,
            SsSqLiteHelper.COLUMN_ACTIVE,
    };
    Connection connection = null;

    public UserRepository()
    {
    }

    public UserEntity GetUser(int userId)
    {
        Connection connection = null;
        UserEntity user = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_User + " where " + SsSqLiteHelper.COLUMN_USERID + " = " + userId);
            user = resultToUser(rs);
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
        return user;
    }

    public UserEntity GetUserByUserName(String userName)
    {
        Connection connection = null;
        UserEntity user = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + SsSqLiteHelper.DB_LOCATION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_User + " where " + SsSqLiteHelper.COLUMN_USERNAME + " = '" + userName + "'");
            user = resultToUser(rs);
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
        return user;
    }

    public boolean CreateUser(UserEntity user)
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

            String sql = " insert into " + TABLE_User + " (" + SsSqLiteHelper.COLUMN_USERID
                    + "," + SsSqLiteHelper.COLUMN_USERNAME
                    + "," + SsSqLiteHelper.COLUMN_USERTYPE
                    + "," + SsSqLiteHelper.COLUMN_PASSWORD
                    + "," + SsSqLiteHelper.COLUMN_ACTIVE
                    + ")"
                    + " values (?, ?, ?, ?, ?)";

            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setInt(1, newId);
            preparedStmt.setString(2, user.getUserName());
            preparedStmt.setInt(3, user.getType());
            preparedStmt.setString(4, user.getPassword());
            preparedStmt.setInt(5, user.getActive());

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

    private int getNewIdentity(Connection connection) throws SQLException {
        int id = 0;

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        ResultSet rs = statement.executeQuery("SELECT max(" + SsSqLiteHelper.COLUMN_USERID + ") as maxid FROM " + TABLE_User);
        if(rs.next()){
            id = rs.getInt("maxid");
        }
        statement.close();
        id++;
        return id;
    }

    private UserEntity resultToUser(ResultSet rs) throws SQLException{
        UserEntity user = new UserEntity();

        user.setUserId(rs.getInt(1));
        user.setUserName(rs.getString(2));
        user.setType(rs.getInt(3));
        user.setPassword(rs.getString(4));
        user.setActive(rs.getInt(5));
        return user;
    }
}
