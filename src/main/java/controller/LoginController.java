package controller;

import controller.annotation.RequestMapping;
import support.Parameter;
import service.UserService;
import webserver.session.SessionManager;
import utils.FileIoUtils;
import webserver.http.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoginController implements Controller {
    private LoginController() {
    }

    public static LoginController getInstance() {
        return LazyHolder.it;
    }

    private static class LazyHolder {
        private static final LoginController it = new LoginController();
    }

    @RequestMapping(path = "/user/login", method = "POST")
    public HttpResponse signIn(Parameter parameter) {
        if (!valid(parameter.get("userId"), parameter.get("password"))) {
            return new ResponseBuilder()
                    .redirect("/user/login_failed.html")
                    .build();
        }
        String sessionId = SessionManager.getInstance().createSession();
        SessionManager.getInstance().findSession(sessionId).setAttribute("logined", true);

        return new ResponseBuilder()
                .cookie(Cookie.setCookie(sessionId))
                .redirect("/index.html")
                .build();
    }

    @RequestMapping(path = "/user/login", method = "GET")
    public HttpResponse accessLoginPage(Parameter parameter) throws IOException, URISyntaxException {
        if (!isLoginUser(parameter.sessionId())) {
            byte[] body = FileIoUtils.mapBody(parameter.get("uri"));
            return new ResponseBuilder()
                    .httpStatus(HttpStatus.OK)
                    .contentType(FileIoUtils.getContentType(parameter.get("uri")))
                    .body(body)
                    .build();
        }
        return new ResponseBuilder()
                .redirect("/index.html")
                .build();
    }

    private boolean valid(String userId, String password) {
        return UserService.getInstance().isExistingUser(userId) &&
                UserService.getInstance().isValidPassword(userId, password);
    }
}
