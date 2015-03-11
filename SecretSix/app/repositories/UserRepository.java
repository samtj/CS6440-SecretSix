package repositories;
import model.UserEntity;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created by Samuel_Tjokrosoesilo on 3/10/2015.
 */
public class UserRepository {

    public UserEntity GetUser(int userId)
    {
        UserEntity user = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            user = (UserEntity)session.load(UserEntity.class,userId);
            Hibernate.initialize(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return user;
    }
}
