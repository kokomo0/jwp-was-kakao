package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.Parameter;
import support.ParameterWrapper;
import webserver.http.Cookie;
import webserver.http.HttpRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParsingUtilsTest {

    @Test
    @DisplayName("쿼리스트링을 파싱한다")
    void parseQuery() {
        Parameter parameter  = ParameterWrapper.wrap(getRequest("a=b&c=d&ee=ff"));
        assertThat(parameter.get("a")).isEqualTo("b");
        assertThat(parameter.get("c")).isEqualTo("d");
        assertThat(parameter.get("ee")).isEqualTo("ff");
    }

    @Test
    @DisplayName("쿼리 스트링 형식이 갖춰지지 않은 경우 예외를 반환한다")
    void parseNotValidQuery() {
        assertThrows(IllegalArgumentException.class,
                () -> ParameterWrapper.wrap(getRequest("a=&=d&ee=ff")));
        assertThrows(IllegalArgumentException.class,
                () -> ParameterWrapper.wrap(getRequest("aaaaaaa=")));
        assertThrows(IllegalArgumentException.class,
                () -> ParameterWrapper.wrap(getRequest("a=b&c=d&")));
    }

    @Test
    @DisplayName("쿠키를 파싱한다")
    void parseCookie() {
        Cookie cookie = Cookie.parseCookie("a=b;c=d;ee=ff");
        assertThat(cookie.get("a")).isEqualTo("b");
        assertThat(cookie.get("c")).isEqualTo("d");
        assertThat(cookie.get("ee")).isEqualTo("ff");
    }
    @Test
    @DisplayName("쿠키 형식이 갖춰지지 않은 경우 예외를 반환한다")
    void parseNotValidCookie() {
        assertThrows(IllegalArgumentException.class,
                () -> Cookie.parseCookie("a=;c=d;=ff"));
        assertThrows(IllegalArgumentException.class,
                () -> Cookie.parseCookie("aaaaaaaa="));
        assertThrows(IllegalArgumentException.class,
                () -> Cookie.parseCookie("=bbbbbbb"));
    }

    private HttpRequest getRequest(String query) {
        return new HttpRequest(null,"/",null,null, query);
    }
}
