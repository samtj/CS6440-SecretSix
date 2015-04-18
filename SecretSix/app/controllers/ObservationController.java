package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.ObservationEntity;
import model.PatientEntity;
import model.StudyEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ObservationRepository;
import repositories.PatientRepository;
import views.html.index;

import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 3/23/2015.
 */
public class ObservationController extends Controller {

    public static Result index() {
        return ok(index.render("Your Observation Controller is ready."));
    }

    //use http://localhost:9000/observation?id=<observationId>
    public static Result GetObservation(String observationId) {
        ObjectNode result = Json.newObject();

        ObservationRepository repository = new ObservationRepository();
        ObservationEntity obs = repository.GetObservation(observationId);

        return ok(Json.toJson(obs));
    }

    public static Result CreateObservation() {
        ObjectNode result = Json.newObject();

        ObservationEntity obs = Json.fromJson(request().body().asJson(), ObservationEntity.class);
        ObservationRepository repository = new ObservationRepository();
        boolean isSaved = repository.CreateObservation(obs);

        result.put("content", isSaved);
        return ok(result);
    }

    public static Result UpdateObservation(String observationId) {
        ObjectNode result = Json.newObject();
        ObservationEntity obs = Json.fromJson(request().body().asJson(), ObservationEntity.class);

        ObservationRepository repository = new ObservationRepository();
        boolean isSaved = repository.SaveObservation(obs);

        result.put("content", isSaved);
        return ok(result);
    }

    public static Result GetObservationsByPatientId(String patientId) {
        ObjectNode result = Json.newObject();

        ObservationRepository repository = new ObservationRepository();
        ArrayList<ObservationEntity> obs = repository.GetObservationsByPatientId(patientId);

        return ok(Json.toJson(obs));
    }
/*
    public static Result GetObservationsByStudyId(String observationId) {
        ObjectNode result = Json.newObject();

        ObservationRepository repository = new ObservationRepository();
        ObservationEntity obs = repository.GetObservation(observationId);

        return ok(Json.toJson(obs));
    }
*/
    public static Result GetAllObservations() {
        return null;
    }
}
