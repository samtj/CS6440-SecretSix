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
        return ok(index.render("Your User Controller is ready."));
    }

    //use http://localhost:9000/user?id=<user_Id>
    public static Result GetTodos() {
        ObjectNode result = Json.newObject();

        // TODO: Add the right SQL query join here
        TodoRepository repository = new TodoRepository();
        ArrayList<TodoEntity> todos = repository.GetTodos();

        result.put("content", "Todo List Here!");

        return ok(result);
    }
}
