package controller;

import db.DataBase;
import model.User;
import utils.ParsingUtils;
import webserver.http.Request;
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

    public Response mapRoute(Request request) {
        String method = request.getMethod();

        String[] paths = request.getUri().split("/", 3);
        String path = paths[2].split("\\?")[0]; //create

        Map<String, String> params;
        if (request.get("Content-Type").equals("application/x-www-form-urlencoded")) {
            params = ParsingUtils.parseQueryString(request.getBody());
        } else {
            params = ParsingUtils.parseQueryString(path.split("\\?", 2)[1]);
        }

        if (method.equals("GET")) {
            if (path.equals("create")) {
                return create(params);
            }
        }
        if (method.equals("POST")) {
            if (path.equals("create")) {
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
