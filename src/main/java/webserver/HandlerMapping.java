package webserver;

import controller.Controller;
import controller.StaticController;
import controller.UserController;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private Map<String, Controller> controllers;

    public HandlerMapping() {
        controllers = new HashMap<>() {{
            put("user", UserController.getInstance());
            put("static", StaticController.getInstance());
        }};
    }

    public Controller getController(String path) {
        return controllers.get(path);
    }
}