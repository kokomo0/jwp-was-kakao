package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import controller.annotation.RequestMapping;
import controller.support.Parameter;
import db.DataBase;
import model.User;
import utils.FileIoUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.io.IOException;

public class UserListController implements Controller {
    private static final UserListController instance = new UserListController();
    private UserListController() {}
    public static UserListController getInstance() {
        return instance;
    }

    @RequestMapping(path="/user/list", method="GET")
    public HttpResponse showUserList(Parameter parameter) {
        if(parameter == null || "".equals(parameter.get("JSESSIONID"))) {
            return new ResponseBuilder()
                    .redirect("/user/login.html")
                    .build();
        }
        try {
            byte[] body = FileIoUtils.mapBody(parameter.get("uri"));
            return new ResponseBuilder()
                    .httpStatus(HttpStatus.OK)
                    .contentType(FileIoUtils.getContentType(parameter.get("uri")))
                    .body(body)
                    .build();
        } catch (Exception e) {
            return new ResponseBuilder().httpStatus(HttpStatus.INTERNET_SERVER_ERROR).build();
        }
    }
}
