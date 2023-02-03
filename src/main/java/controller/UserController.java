package controller;

import db.DataBase;
import model.User;

import java.util.Map;

public class UserController implements Controller {
    private static UserController instance;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public byte[] mapRoute(String path, Map<String, String> params) {
        if (path.equals("create")) {
            return create(params);
        }
        return new byte[] {};
    }

    public byte[] create(Map<String, String> params) {
        User newUser = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(newUser);
        return new byte[] {};
    }
}
