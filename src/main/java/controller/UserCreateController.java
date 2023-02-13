package controller;

import controller.annotation.RequestMapping;
import service.UserService;
import support.Parameter;
import webserver.http.*;

public class UserCreateController implements Controller {
    public static String successUri = "/index.html";
    public static String failUri = "/user/form.html";

    private UserCreateController() {
    }

    public static UserCreateController getInstance() {
        return UserCreateController.LazyHolder.it;
    }

    private static class LazyHolder {
        private static final UserCreateController it = new UserCreateController();
    }

    @RequestMapping(path = "/user/create", method = "POST")
    public HttpResponse createUserByPost(Parameter parameter) {
        UserService.getInstance().createUser(parameter);
        return new ResponseBuilder()
                .redirect(successUri)
                .build();
    }

    @RequestMapping(path = "/user/create", method = "GET")
    public HttpResponse createUserByGet(Parameter parameter) {
        UserService.getInstance().createUser(parameter);
        return new ResponseBuilder()
                .redirect(successUri)
                .build();
    }
}
