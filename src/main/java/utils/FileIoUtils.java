package utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIoUtils {
    public static boolean exists(String filePath) {
        if (filePath.equals("/"))
            return true;
        return new File("src/main/resources/templates" + filePath).exists() || new File("src/main/resources/static" + filePath).exists();
    }

    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException, NullPointerException {
        Path path = Paths.get(FileIoUtils.class.getClassLoader().getResource(filePath).toURI());
        return Files.readAllBytes(path);
    }
}
