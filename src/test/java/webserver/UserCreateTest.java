package webserver;

import db.DataBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCreateTest {
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
                        "Content-Length: 92\n" +
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

    @DisplayName("쿼리문 형식이 이상하다")
    @Test
    void badQuery() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/create?userId=1&password=&name==&&&email= HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /user/form.html ");

    }

    @DisplayName("유저를 만드는데 필요한 파라미터가 다 들어오지 않았다")
    @Test
    void badParameter() {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/create?userId=a&password=123&qwer=asdf&zxcv=uiop HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();
        List<String> response = Arrays.asList(socket.output().split("\r\n"));
        assertThat(response.get(0)).isEqualTo("HTTP/1.1 302 Found ");
        assertThat(response.stream().filter(s -> s.contains("Location")).findAny().get()).isEqualTo("Location: /user/form.html ");

    }
}
