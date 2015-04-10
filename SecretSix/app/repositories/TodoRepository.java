package repositories;

import model.StudyEntity;

import java.sql.Connection;

/**
 * Created by Samuel_Tjokrosoesilo on 4/9/2015.
 */
public class TodoRepository {
    public class StudyRepository {
        public static final String TABLE_Study = "Study";
        public static final String TABLE_Patient = "Patient";
        public static final String TABLE_Observation = "Observation";
        Connection connection = null;

        public StudyEntity GetTodos()
        {
            return null;
        }
    }
}
