package controller;

import db.DataBase;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.util.Map;

import static utils.ParsingUtils.parseParameter;

public class UserController implements Controller {
    private static final UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    public HttpResponse mapRoute(HttpRequest httpRequest) {
        String path = httpRequest.getUri().split("\\?", 2)[0];
        Map<String, String> params = parseParameter(httpRequest);

        if (path.equals("/user/create")) {
            return create(params);
        }
        return new ResponseBuilder().httpStatus(HttpStatus.NOT_FOUND).build();
    }

    public HttpResponse create(Map<String, String> params) {
        User newUser = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(newUser);
        return new ResponseBuilder().httpStatus(HttpStatus.FOUND).location("/index.html").build();
    }
}
