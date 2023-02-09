package controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

public class UserListController implements Controller {
    private static final UserListController instance = new UserListController();
    private UserListController() {}
    public static UserListController getInstance() {
        return instance;
    }


    public HttpResponse handleRequest(HttpRequest httpRequest) {
        String cookie = httpRequest.get("Cookie");
        if("".equals(cookie)||!httpRequest.get("Cookie").contains("logined=true")) {
            return new ResponseBuilder()
                    .httpVersion(httpRequest.getHttpVersion())
                    .httpStatus(HttpStatus.FOUND)
                    .location("/user/login.html")
                    .build();
        }
        return new ResponseBuilder()
                .httpVersion(httpRequest.getHttpVersion())
                .httpStatus(HttpStatus.FOUND)
                .location("/user/list.html")
                .build();
    }
}
