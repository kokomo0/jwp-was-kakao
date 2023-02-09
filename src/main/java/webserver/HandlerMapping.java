package webserver;

import controller.*;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, Controller> controllers;

    public HandlerMapping() {
        controllers = new HashMap<>() {{
            put("user", UserController.getInstance());
            put("resource", ResourceController.getInstance());
            //put("login", LoginController.getInstance());
            //put("list", UserListController.getInstance());
            put("hello", HomeController.getInstance());
        }};
    }

    public Controller getController(String path) {
        return controllers.get(path);
    }
}
