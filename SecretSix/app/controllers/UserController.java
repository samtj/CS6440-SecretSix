package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.PatientEntity;
import model.StudyEntity;
import model.UserEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.StudyRepository;
import repositories.UserRepository;
import views.html.index;

/**
 * Created by Samuel_Tjokrosoesilo on 3/10/2015.
 */
public class UserController extends Controller {
    public static Result index() {
        return ok(index.render("Your User Controller is ready."));
    }

    //use http://localhost:9000/user?id=<user_Id>
    public static Result GetUser(int userId) {
        ObjectNode result = Json.newObject();

        // TODO: get from user repository
        UserRepository repository = new UserRepository();
        UserEntity user = repository.GetUser(userId);

        return ok(Json.toJson(user));
    }

    public static Result AuthenticateUser() {
        ObjectNode result = Json.newObject();

        UserEntity user = Json.fromJson(request().body().asJson(), UserEntity.class);
        UserRepository repository = new UserRepository();

        UserEntity retrievedUser = repository.GetUserByUserName(user.getUserName());
        if (retrievedUser != null && user.getPassword().equals(retrievedUser.getPassword()))
            retrievedUser.setPassword("");
        else
            retrievedUser = null;

        if (retrievedUser != null)
            return ok(Json.toJson(retrievedUser));
        else
            result.put("content", "authentication failure");
            return ok(result);
    }

    public static Result CreateUser() {
        ObjectNode result = Json.newObject();

        UserEntity user = Json.fromJson(request().body().asJson(), UserEntity.class);
        UserRepository repository = new UserRepository();
        boolean isSaved = repository.CreateUser(user);

        result.put("content", isSaved);
        return ok(result);
    }
}
