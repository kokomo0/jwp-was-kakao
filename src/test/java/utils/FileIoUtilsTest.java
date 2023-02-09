package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileIoUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(FileIoUtilsTest.class);

    @Test
    @DisplayName("파일을 잘 가져오는지 확인")
    void loadFileFromClasspath() throws Exception {
        byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
        log.debug("file : {}", new String(body));
    }

    @Test
    @DisplayName("파일이 없는 경우 예외를 반환")
    void loadMissingFileFromClasspath() throws Exception {
        assertThrows(NullPointerException.class,
                () -> FileIoUtils.loadFileFromClasspath("./abcd.html"));
    }
}
