package model;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    /**
     * @throws IllegalArgumentException: 유저를 만드는데 필요한 파라미터가 제대로 들어오지 않음
     */
    public User(String userId, String password, String name, String email) throws IllegalArgumentException {
        if (userId == null || password == null || name == null || email == null)
            throw new IllegalArgumentException();
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
