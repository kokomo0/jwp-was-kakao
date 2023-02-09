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
//
//    public HttpResponse handleRequest(HttpRequest httpRequest) {
//        String cookie = httpRequest.get("Cookie");
//        if("".equals(cookie)||!httpRequest.get("Cookie").contains("logined=true")) {
//            return new ResponseBuilder()
//                    .httpVersion(httpRequest.getHttpVersion())
//                    .httpStatus(HttpStatus.FOUND)
//                    .location("/user/login.html")
//                    .build();
//        }
//        return new ResponseBuilder()
//                .httpVersion(httpRequest.getHttpVersion())
//                .httpStatus(HttpStatus.OK)
//                .build();
//
//        try {
//            TemplateLoader loader = new ClassPathTemplateLoader();
//            loader.setPrefix("/templates");
//            loader.setSuffix(".html");
//            Handlebars handlebars = new Handlebars(loader);
//            Template template = handlebars.compile("user/list");
//
//            String profilePage = template.apply(DataBase.findAll());
//
//            return new ResponseBuilder()
//                    .httpVersion(httpRequest.getHttpVersion())
//                    .httpStatus(HttpStatus.OK)
//                    .body(profilePage.getBytes())
//                    .build();
//        } catch(IOException e) {
//            return new ResponseBuilder().httpStatus(HttpStatus.INTERNET_SERVER_ERROR).build();
//        }
//    }
}
