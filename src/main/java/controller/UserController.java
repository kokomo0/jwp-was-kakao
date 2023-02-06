package controller;

import db.DataBase;
import model.User;
import webserver.http.Request;
import webserver.http.Response;
import webserver.http.ResponseBuilder;

import java.util.Map;

import static utils.ParsingUtils.parseParameter;

public class UserController implements Controller {
    private static UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    public Response mapRoute(Request request) {
        String path = request.getUri().split("\\?", 2)[0];
        Map<String, String> params = parseParameter(request);

        if (path.equals("/user/create")) {
            return create(params);
        }
        return new ResponseBuilder().httpStatus("404 Not Found").build();
    }

    public Response create(Map<String, String> params) {
        User newUser = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(newUser);
        return new ResponseBuilder().httpStatus("302 Found").location("/index.html").build();
    }
}
