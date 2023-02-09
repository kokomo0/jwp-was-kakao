package controller;

import controller.annotation.RequestMapping;
import controller.support.Parameter;
import utils.FileIoUtils;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class UserListController implements Controller {
    private UserListController() {
    }

    public static UserListController getInstance() {
        return LazyHolder.it;
    }

    private static class LazyHolder {
        private static final UserListController it = new UserListController();
    }

    @RequestMapping(path = "/user/list", method = "GET")
    public HttpResponse showUserList(Parameter parameter) throws IOException, URISyntaxException {
        if (!isLoginUser(parameter.sessionId())) {
            return new ResponseBuilder()
                    .redirect("/user/login.html")
                    .build();
        }
        byte[] body = FileIoUtils.mapBody(parameter.get("uri"));
        return new ResponseBuilder()
                .httpStatus(HttpStatus.OK)
                .contentType(FileIoUtils.getContentType(parameter.get("uri")))
                .body(body)
                .build();
    }
}
