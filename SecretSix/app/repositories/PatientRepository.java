package repositories;

import model.PatientEntity;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
/**
 * Created by Samuel_Tjokrosoesilo on 3/1/2015.
 */
public class PatientRepository {
    public PatientEntity GetPatient(int patientId)
    {
        PatientEntity patient = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            System.out.println("querying all the managed entities...");
            patient = (PatientEntity)session.load(PatientEntity.class,patientId);
            Hibernate.initialize(patient);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return patient;
    }

    public int SavePatient(PatientEntity patient)
    {
        PatientEntity entity = new PatientEntity();

        // TODO: Pull from DB
        return entity.getPatientId();
    }
}
