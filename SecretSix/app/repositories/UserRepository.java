package repositories;
import model.UserEntity;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
