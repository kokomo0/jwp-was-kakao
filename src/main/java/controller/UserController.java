package controller;

import controller.annotation.RequestMapping;
import model.User;
import service.UserService;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.util.Map;

import static utils.ParsingUtils.parseParameter;

public class UserController implements Controller {
    private final UserService userService = UserService.getInstance();

    private UserController() {
    }

    public static UserController getInstance() {
        return UserController.LazyHolder.it;
    }

    private static class LazyHolder {
        private static final UserController it = new UserController();
    }

    @RequestMapping(path = "/user/create", method = "POST")
    public HttpResponse createUserByPost(User user) {
        userService.add(user);
        return new ResponseBuilder()
                .redirect("/index.html")
                .build();
    }

    @RequestMapping(path = "/user/create", method = "GET")
    public HttpResponse createUserByGet(User user) {
        userService.add(user);
        return new ResponseBuilder()
                .redirect("/index.html")
                .build();
    }


    public HttpResponse handleRequest(HttpRequest httpRequest) {
        String path = httpRequest.getUri().split("\\?", 2)[0];
        Map<String, String> params = parseParameter(httpRequest);

        if (path.equals("/user/create")) {
            userService.create(params);
            return new ResponseBuilder()
                    .httpVersion(httpRequest.getHttpVersion())
                    .httpStatus(HttpStatus.FOUND)
                    .location("/index.html")
                    .build();
        }
        return new ResponseBuilder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }
}
