package webserver;

import controller.*;
import webserver.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import static utils.FileIoUtils.exists;

public class HandlerMapping {
    private final Map<String, Controller> controllers;

    public HandlerMapping() {
        controllers = new HashMap<>() {{
            put("user", UserController.getInstance());
            put("resource", ResourceController.getInstance());
            put("login", LoginController.getInstance());
            put("list", UserListController.getInstance());
            put("home", HomeController.getInstance());
        }};
    }

    public Controller map(HttpRequest httpRequest) {
        String uri = httpRequest.getUri();
        if (uri.startsWith("/user/create"))
            return controllers.get("user");
        if (uri.equals("/user/login"))
            return controllers.get("login");
        if (uri.startsWith("/user/list"))
            return controllers.get("list");
        if (!uri.equals("/") && exists(uri))
            return controllers.get("resource");
        return controllers.get("home");
    }
}
