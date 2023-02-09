package service;

import db.DataBase;
import model.User;

import java.util.Collection;
import java.util.Map;

public class UserService {
    private static final UserService instance = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
        return instance;
    }

    public User create(Map<String, String> params) {
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        return user;
    }

    public void add(User user) {
        DataBase.addUser(user);
    }

    public boolean isExistingUser(String id) {
        return DataBase.findUserById(id) != null;
    }

    public boolean isValidPassword(String userId, String password) {
        return DataBase.findUserById(userId).getPassword().equals(password);
    }

    public Collection<User> getAllUsers() {
        return DataBase.findAll();
    }
}
