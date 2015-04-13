package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.TodoEntity;
import model.UserEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.TodoRepository;
import repositories.UserRepository;
import views.html.index;

import java.util.ArrayList;

/**
 * Created by Samuel_Tjokrosoesilo on 4/9/2015.
 */
public class TodoController extends Controller {
    public static Result index() {
        return ok(index.render("Your Todo Controller is ready."));
    }

    public static Result GetTodos() {
        ObjectNode result = Json.newObject();

        boolean includeAll = false;
        TodoRepository repository = new TodoRepository();
        ArrayList<TodoEntity> todos = repository.GetTodos(includeAll);

        return ok(Json.toJson(todos));
    }

    public static Result GetAllTodos() {
        ObjectNode result = Json.newObject();

        boolean includeAll = true;
        TodoRepository repository = new TodoRepository();
        ArrayList<TodoEntity> todos = repository.GetTodos(includeAll);

        return ok(Json.toJson(todos));
    }
}
