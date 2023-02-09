package utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileIoUtils {
    public static boolean exists(String filePath) {
        if (filePath.equals("/"))
            return true;
        return new File("src/main/resources/templates" + filePath).exists() ||
                new File("src/main/resources/static" + filePath).exists();
    }

    /**
     * @throws IOException: Files.readAllBytes()에서 발생할 수 있는 IO 예외
     * @throws URISyntaxException: URI.toURI() 실행 시 uri가 제대로 전달되지 않았다면 발생하는 있는 예외
     * @throws NullPointerException: URL.getResource() 실행 시 파일이 존재하지 않는다면 발생하는 예외
     */
    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException, NullPointerException {
        Path path = Paths.get(Objects.requireNonNull(FileIoUtils.class.getClassLoader().getResource(filePath)).toURI());
        return Files.readAllBytes(path);
    }

    public static byte[] mapBody(String path) throws IOException, URISyntaxException {
        if (path.equals("/")) {
            return "Hello world".getBytes();
        }
        if (path.endsWith("html")) {
            return FileIoUtils.loadFileFromClasspath("templates" + path);
        }
        return FileIoUtils.loadFileFromClasspath("static" + path);
    }

    public static String getContentType(String path) {
        Map<String, String> contentTypes = new HashMap<>() {{
            put("html", "text/html");
            put("css", "text/css");
            put("js", "text/javascript");
            put("png", "image/png");
        }};
        String[] filePath = path.split("\\.");
        return contentTypes.getOrDefault(filePath[filePath.length - 1], "text/plain");
    }
}
