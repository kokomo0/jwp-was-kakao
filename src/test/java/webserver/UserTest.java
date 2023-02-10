package webserver;

import db.DataBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @DisplayName("GET 방식으로 유저를 생성한다")
    @Test
    void createUserByGet() {
        final String httpRequest = String.join("\n",
                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1\n",
                "Host: localhost:8080\n",
                "Connection: keep-alive\n",
                "\n",
                "\n");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();

        List<String> response = Arrays.asList(socket.output().split("\r\n"));

        assertThat(response.get(0).equals("HTTP/1.1 302 Found"));
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /index.html ");
        assertThat(DataBase.findUserById("cu").getPassword()).isEqualTo("password");
    }

    @DisplayName("POST 방식으로 유저를 생성한다")
    @Test
    void createUserByPost() {
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Connection: keep-alive\n" +
                        "Content-Length: 59\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Accept: */*\n" +
                        "\n" +
                        "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));

        assertThat(response.get(0).equals("HTTP/1.1 302 Found"));
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /index.html ");
        assertThat(DataBase.findUserById("cu").getPassword()).isEqualTo("password");
    }

    @Test
    void loginSuccess() {
        createUserByGet();
        String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1\r\n" +
                        "Referer: http://localhost:8080/user/login.html\r\n" +
                        "Origin: http://localhost:8080\r\n" +
                        "Content-Type: application/x-www-form-urlencoded\r\n" +
                        "Content-Length: 28\r\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                        "\r\n" +
                        "userId=cu&password=password");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /index.html ");
    }

    @Test
    void loginFailed() {
        createUserByGet();
        String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1\r\n" +
                        "Referer: http://localhost:8080/user/login.html\r\n" +
                        "Origin: http://localhost:8080\r\n" +
                        "Content-Type: application/x-www-form-urlencoded\r\n" +
                        "Content-Length: 28\r\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                        "\r\n" +
                        "userId=cu&password=password123");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /user/login_failed.html ");

    }


}
