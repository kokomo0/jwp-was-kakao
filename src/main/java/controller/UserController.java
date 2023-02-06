package controller;

import db.DataBase;
import model.User;
import webserver.http.Response;
import webserver.http.ResponseBuilder;

import java.util.Map;

public class UserController implements Controller {
    private static UserController instance;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public Response mapRoute(String method, String path, Map<String, String> params) {
        if(method.equals("GET")) {
            if (path.equals("create")) {
                return create(params);
            }
        }
        if(method.equals("POST" )) {
            if(path.equals("create")) {
                return create(params);
            }
        }
        return new ResponseBuilder().httpStatus("404 Not Found").build();
    }

    public Response create(Map<String, String> params) {
        User newUser = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(newUser);
        return new ResponseBuilder().httpStatus("302 Found").location("/index.html").build();
    }
}
