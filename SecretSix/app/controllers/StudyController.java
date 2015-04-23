package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.PatientEntity;
import model.StudyEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.PatientRepository;
import repositories.StudyRepository;
import views.html.index;

import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 4/7/2015.
 */
public class StudyController extends Controller {
        public static Result index() {
            return ok(index.render("Your Study Controller is ready."));
        }

        //use http://localhost:9000/study?id=<study_Id>
        public static Result GetStudy(int studyId) {
            ObjectNode result = Json.newObject();

            StudyRepository repository = new StudyRepository();
            StudyEntity study = repository.GetStudy(studyId);

            return ok(Json.toJson(study));
        }

        public static Result CreateStudy() {
            ObjectNode result = Json.newObject();

            StudyEntity study = Json.fromJson(request().body().asJson(), StudyEntity.class);
            StudyRepository repository = new StudyRepository();
            boolean isSaved = repository.CreateStudy(study);

            result.put("content", isSaved);
            return ok(result);
        }

        public static Result UpdateStudy(int studyId) {
            ObjectNode result = Json.newObject();
            StudyEntity study = Json.fromJson(request().body().asJson(), StudyEntity.class);

            StudyRepository repository = new StudyRepository();
            boolean isSaved = repository.SaveStudy(study);

            result.put("content", isSaved);
            return ok(result);
        }

        public static Result GetAllStudies() {

            ObjectNode result = Json.newObject();

            StudyRepository repository = new StudyRepository();
            ArrayList<StudyEntity> studies = repository.GetStudies();

            return ok(Json.toJson(studies));
        }

        public static Result GetCount() {

            ObjectNode result = Json.newObject();

            StudyRepository repository = new StudyRepository();
            ArrayList<StudyEntity> studies = repository.GetStudies();

            return ok(Json.toJson(studies.size()));
        }

        public static Result GetReportStudies() {

            ObjectNode result = Json.newObject();

            StudyRepository repository = new StudyRepository();
            ArrayList<StudyEntity> studies = repository.GetReportStudies();

            return ok(Json.toJson(studies));
        }
    }

