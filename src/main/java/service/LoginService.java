package service;

public class LoginService {
    private static final LoginService instance = new LoginService();
    private LoginService() {}
    public static LoginService getInstance() {
        return instance;
    }
}
