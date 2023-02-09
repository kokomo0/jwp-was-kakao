package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import controller.annotation.RequestMapping;
import service.UserService;
import support.Parameter;
import utils.FileIoUtils;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.io.IOException;

public class UserListController implements Controller {
    private final UserService userService = UserService.getInstance();
    private UserListController() {
    }

    public static UserListController getInstance() {
        return LazyHolder.it;
    }

    private static class LazyHolder {
        private static final UserListController it = new UserListController();
    }

    @RequestMapping(path = "/user/list", method = "GET")
    public HttpResponse showUserList(Parameter parameter) throws IOException {
        if (!isLoginUser(parameter.sessionId())) {
            return new ResponseBuilder()
                    .redirect("/user/login.html")
                    .build();
        }

        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);
        Template template = handlebars.compile("user/list");
        String users = template.apply(userService.getAllUsers());

        return new ResponseBuilder()
                .httpStatus(HttpStatus.OK)
                .contentType(FileIoUtils.getContentType(parameter.get("uri")))
                .body(users.getBytes())
                .build();
    }
}
