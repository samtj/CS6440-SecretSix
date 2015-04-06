package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.PatientEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.PatientRepository;
import views.html.index;

import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 3/1/2015.
*/
public class PatientController extends Controller {


    public static Result index() {
        return ok(index.render("Your Patient Controller is ready."));
    }



    //use http://localhost:9000/patient?id=<patient_Id>
    public static Result GetPatient(String patientId) {
        ObjectNode result = Json.newObject();

        PatientRepository repository = new PatientRepository();
        PatientEntity patient = repository.GetPatient(patientId);

        return ok(Json.toJson(patient));
    }

    public static Result CreatePatient() {
        ObjectNode result = Json.newObject();

        PatientEntity patient = Json.fromJson(request().body().asJson(), PatientEntity.class);
        PatientRepository repository = new PatientRepository();
        boolean isSaved = repository.CreatePatient(patient);

        result.put("content", isSaved);
        return ok(result);
    }

    public static Result UpdatePatient(String patientId) {
        ObjectNode result = Json.newObject();
        PatientEntity patient = Json.fromJson(request().body().asJson(), PatientEntity.class);

        PatientRepository repository = new PatientRepository();
        boolean isSaved = repository.SavePatient(patient);

        result.put("content", isSaved);
        return ok(result);
    }

    public static Result GetAllPatients() {

        ObjectNode result = Json.newObject();

        PatientRepository repository = new PatientRepository();
        ArrayList<PatientEntity> patients = repository.GetPatients();

        return ok(Json.toJson(patients));
    }
    public static Result GetCount() {

        ObjectNode result = Json.newObject();

        PatientRepository repository = new PatientRepository();
        ArrayList<PatientEntity> patients = repository.GetPatients();

        return ok(Json.toJson(patients.size()));
    }
}
