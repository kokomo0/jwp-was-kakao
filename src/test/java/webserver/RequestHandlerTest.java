package webserver;

import db.DataBase;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/plain;charset=utf-8 ",
                "Content-Length: 11 ",
                "",
                "Hello world");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 6902 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void createUserByGet() {
        final String httpRequest = String.join("\r\n",
                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        handler.run();

        assertThat(DataBase.findUserById("cu").getPassword()).isEqualTo("password");
    }

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