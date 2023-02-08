package controller;

import db.DataBase;
import service.LoginService;
import webserver.http.*;

import java.util.Map;

import static utils.ParsingUtils.parseQueryString;

public class LoginController implements Controller {
    private final static LoginController instance = new LoginController();
    private final LoginService loginService = LoginService.getInstance();

    private LoginController() {
    }

    public static LoginController getInstance() {
        return instance;
    }

    public HttpResponse handleRequest(HttpRequest httpRequest) {
        if(!"".equals(httpRequest.get("Cookie")) && httpRequest.get("Cookie").contains("JSESSIONID")) {
            return loginByCookie(httpRequest);
        }
        Map<String, String> params = parseQueryString(httpRequest.getBody());
        if (!DataBase.findUserById(params.get("userId")).getPassword().equals(params.get("password"))) {
            return new ResponseBuilder()
                    .httpVersion(httpRequest.getHttpVersion())
                    .httpStatus(HttpStatus.FOUND)
                    .location("/user/login_failed.html")
                    .build();
        }
        return new ResponseBuilder()
                .httpVersion(httpRequest.getHttpVersion())
                .httpStatus(HttpStatus.FOUND)
                .location("/index.html")
                .cookie(new Cookie())
                .build();
    }

    private HttpResponse loginByCookie(HttpRequest httpRequest) {
        Cookie cookie = new Cookie(httpRequest.get("Cookie"));
        return new ResponseBuilder()
                .httpVersion(httpRequest.getHttpVersion())
                .httpStatus(HttpStatus.FOUND)
                .location("/index.html")
                .build();
    }

}
