package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FileIoUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(FileIoUtilsTest.class);

    @Test
    @DisplayName("파일을 잘 가져오는지 확인한다")
    void loadFileFromClasspath() throws Exception {
        byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
        log.debug("file : {}", new String(body));
    }

    @Test
    @DisplayName("파일이 없는 경우 예외를 반환한다")
    void loadMissingFileFromClasspath() throws Exception {
        assertThrows(NullPointerException.class,
                () -> FileIoUtils.loadFileFromClasspath("./abcd.html"));
    }

    @Test
    @DisplayName("(폴더가 아닌)파일이 있는지 확인한다")
    void checkExistence(){
        assertTrue(FileIoUtils.exists("/css/styles.css"));
        assertFalse(FileIoUtils.exists("/css"));
    }

    @Test
    @DisplayName("콘텐트 타입을 잘 가져온다")
    void getContentType(){
        assertThat(FileIoUtils.getContentType("scripts1234/abcd/scripts.js")).isEqualTo("text/javascript");
        assertThat(FileIoUtils.getContentType("/css/styles.css")).isEqualTo("text/css");
        assertThat(FileIoUtils.getContentType("/")).isEqualTo("text/plain");
    }
}
