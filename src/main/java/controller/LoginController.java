package controller;

import controller.annotation.RequestMapping;
import controller.support.Parameter;
import service.UserService;
import webserver.session.Session;
import webserver.session.SessionManager;
import utils.FileIoUtils;
import webserver.http.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoginController implements Controller {
    private final UserService userService = UserService.getInstance();

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
        if (!userService.isExistingUser(parameter.get("userId")) ||
                !userService.isValidPassword(parameter.get("userId"), parameter.get("password"))) {
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
    public HttpResponse accessLoginPage(Parameter parameter) throws IOException, URISyntaxException, NullPointerException {
        if (parameter == null || "".equals(parameter.get("JSESSIONID"))) {
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


    private boolean isLoginUser(HttpRequest request) {
        if (!request.hasCookie())
            return false;
        Cookie cookie = Cookie.parseCookie(request.get("Cookie"));
        String sessionId = Cookie.parseCookie(request.get("Cookie")).get("JSESSIONID");
        Session session = SessionManager.getInstance().findSession(sessionId);
        return session != null && (boolean) session.getAttribute("logined");
    }

}
