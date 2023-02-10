package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest {
    private String postRequest = String.join("\r\n",
            "POST /user/login HTTP/1.1\r\n" +
                    "Referer: http://localhost:8080/user/login.html\r\n" +
                    "Origin: http://localhost:8080\r\n" +
                    "Content-Type: application/x-www-form-urlencoded\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");

    private String getRequest = String.join("\r\n",
            "GET /user/login.html HTTP/1.1\r\n" +
            "Referer: http://localhost:8080/user/login.html\r\n" +
            "Origin: http://localhost:8080\r\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");

    private String getRequestforUserList = String.join("\r\n",
            "GET /user/list.html HTTP/1.1\r\n" +
                    "Referer: http://localhost:8080/user/login.html\r\n" +
                    "Origin: http://localhost:8080\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");

    @DisplayName("로그인에 성공하고 쿠키 발급을 잘 받았다")
    @Test
    void loginSuccess() {
        createUserByGet();
        String httpRequest = postRequest +
                "Content-Length: 27\r\n\r\n" +
                "userId=cu&password=password";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /index.html ");
        assertThat(response.stream().filter(s -> s.contains("JSESSIONID")).collect(Collectors.toList())).hasSize(1);
    }

    @DisplayName("비밀번호가 틀려서 로그인에 실패하고 성공하고 쿠키 발급을 못 받았다")
    @Test
    void loginFailedWrongPassword() {
        createUserByGet();
        String httpRequest = postRequest +
                "Content-Length: 30\r\n\r\n" +
                "userId=cu&password=password123";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /user/login_failed.html ");
        assertThat(response.stream().filter(s -> s.contains("JSESSIONID")).collect(Collectors.toList())).hasSize(0);
    }

    @DisplayName("존재하지 않는 아이디여서 로그인에 실패하고 성공하고 쿠키 발급을 못 받았다")
    @Test
    void loginFailedNotExistingUser() {
        String httpRequest = postRequest +
                "Content-Length: 30\r\n\r\n" +
                "userId=kayla&password=password";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /user/login_failed.html ");
        assertThat(response.stream().filter(s -> s.contains("JSESSIONID")).collect(Collectors.toList())).hasSize(0);
    }

    @DisplayName("로그인하여 쿠키를 발급 받을 경우, 로그인 페이지를 누르면 index.html로 돌아간다")
    @Test
    void isLoginUser() {
        String cookie = Arrays.asList(login().split("\r\n"))
                .stream().filter(s -> s.contains("JSESSIONID")).findAny().get()
                .split(":")[1];

        String httpRequest = getRequest+ "Cookie: " + cookie + "\r\n\r\n";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /index.html ");
    }

    @DisplayName("잘못된 쿠키를 가지고 있는 유저가 로그인 페이지에 접속한다")
    @Test
    void isNotValidLoginUser() {
        login();
        String cookie = "JSESSIONID=" + UUID.randomUUID();

        String httpRequest = getRequest+ "Cookie: " + cookie + "\r\n\r\n";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 200 OK ");
    }

    @DisplayName("쿠키가 없는 유저가 로그인 페이지에 접속한다")
    @Test
    void isNotLoginUser() {
        login();

        String httpRequest = getRequest + "\r\n\r\n";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 200 OK ");
    }

    @DisplayName("로그인하여 쿠키를 발급 받을 경우, 유저 리스트 페이지를 누르면 유저 리스트가 보여진다")
    @Test
    void isLoginUserForUserList() {
        String cookie = Arrays.asList(login().split("\r\n"))
                .stream().filter(s -> s.contains("JSESSIONID")).findAny().get()
                .split(":")[1];

        String httpRequest = getRequestforUserList+ "Cookie: " + cookie + "\r\n\r\n";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 200 OK ");
    }

    @DisplayName("잘못된 쿠키를 가지고 있는 유저 리스트에 접속한다")
    @Test
    void isNotValidLoginUserForUserList() {
        login();
        String cookie = "JSESSIONID=" + UUID.randomUUID();

        String httpRequest = getRequestforUserList+ "Cookie: " + cookie + "\r\n\r\n";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /user/login.html ");
    }

    @DisplayName("쿠키가 없는 유저가 유저 리스트에 접속한다")
    @Test
    void isNotLoginUserForUserList() {
        login();

        String httpRequest = getRequestforUserList + "\r\n\r\n";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /user/login.html ");

    }



    private void createUserByGet() {
        final String httpRequest = String.join("\n",
                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1\n",
                "Host: localhost:8080\n",
                "Connection: keep-alive\n",
                "\n",
                "\n");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
    }

    String login() {
        createUserByGet();
        String httpRequest = postRequest +
                "Content-Length: 27\r\n\r\n" +
                "userId=cu&password=password";
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);
        handler.run();
        return socket.output();
    }
}
