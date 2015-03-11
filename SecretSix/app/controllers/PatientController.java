package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.PatientEntity;
import play.*;
import play.libs.Json;
import play.mvc.*;

import repositories.PatientRepository;
import views.html.*;

/**
 * Created by Samuel_Tjokrosoesilo on 3/1/2015.
*/
public class PatientController extends Controller {


    public static Result index() {
        return ok(index.render("Your Patient Controller is ready."));
    }

    //use http://localhost:9000/patient?id=<patient_Id>
    public static Result GetPatient(int patientId) {
        ObjectNode result = Json.newObject();

        // TODO: get from patient repository
        PatientRepository repository = new PatientRepository();
        PatientEntity patient = repository.GetPatient(patientId);

        result.put("content", patient.getName());

        return ok(result);
    }

    public static Result GetAllPatients() {
        return null;
    }
}
