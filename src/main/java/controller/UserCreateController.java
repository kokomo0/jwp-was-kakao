package controller;

import controller.annotation.RequestMapping;
import model.User;
import service.UserService;
import webserver.http.*;

public class UserCreateController implements Controller {
    private final UserService userService = UserService.getInstance();

    private UserCreateController() {
    }

    public static UserCreateController getInstance() {
        return UserCreateController.LazyHolder.it;
    }

    private static class LazyHolder {
        private static final UserCreateController it = new UserCreateController();
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
}
