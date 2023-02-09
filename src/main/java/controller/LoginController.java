package controller;

import controller.annotation.RequestMapping;
import controller.support.Parameter;
import db.DataBase;
import service.LoginService;
import service.UserService;
import session.Session;
import session.SessionManager;
import webserver.http.*;

import java.util.Map;

import static utils.ParsingUtils.parseQueryString;

public class LoginController implements Controller {
    private final static LoginController instance = new LoginController();
    private final UserService userService = UserService.getInstance();

    private LoginController() {
    }

    public static LoginController getInstance() {
        return instance;
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


    private boolean isLoginUser(HttpRequest request) {
        if (!request.hasCookie())
            return false;
        Cookie cookie = Cookie.parseCookie(request.get("Cookie"));
        String sessionId = Cookie.parseCookie(request.get("Cookie")).get("JSESSIONID");
        Session session = SessionManager.getInstance().findSession(sessionId);
        return session != null && (boolean) session.getAttribute("logined");
    }
//
//    public HttpResponse handleRequest(HttpRequest httpRequest) {
//        if (!"".equals(httpRequest.get("Cookie")) && httpRequest.get("Cookie").contains("JSESSIONID")) {
//            return loginByCookie(httpRequest);
//        }
//        Map<String, String> params = parseQueryString(httpRequest.getBody());
//        if (!DataBase.findUserById(params.get("userId")).getPassword().equals(params.get("password"))) {
//            return new ResponseBuilder()
//                    .httpVersion(httpRequest.getHttpVersion())
//                    .httpStatus(HttpStatus.FOUND)
//                    .location("/user/login_failed.html")
//                    .build();
//        }
//        return new ResponseBuilder()
//                .httpVersion(httpRequest.getHttpVersion())
//                .httpStatus(HttpStatus.FOUND)
//                .location("/index.html")
//                .cookie(new Cookie())
//                .build();
//    }
//
//    private HttpResponse loginByCookie(HttpRequest httpRequest) {
//        Cookie cookie = new Cookie(httpRequest.get("Cookie"));
//        return new ResponseBuilder()
//                .httpVersion(httpRequest.getHttpVersion())
//                .httpStatus(HttpStatus.FOUND)
//                .location("/index.html")
//                .build();
//    }

}
