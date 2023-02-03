package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class IOUtils {
    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static Map<String, String> readRequestHeader(BufferedReader br) throws IOException {
        List<String> requestHeader = new ArrayList<>();
        String line = br.readLine();
        while (!"".equals(line) || line == null) {
            requestHeader.add(line);
            line = br.readLine();
        }
        return parseHeader(requestHeader);
    }

    public static List<String> getMethodAndPath(String line) {
        return Arrays.asList(line.split(" "));
    }

    public static Map<String, String> parseHeader(List<String> rawHeader) {
        Map<String, String> headers = new HashMap<>();
        String[] startLine = rawHeader.get(0).split(" ");
        headers.put("method", startLine[0]);
        headers.put("path", startLine[1]);
        headers.put("protocol version", startLine[2]);

        for (String header : rawHeader.subList(1, rawHeader.size())) {
            System.out.println(header);
            String[] line = header.split(":", 2);
            headers.put(line[0].trim(), line[1].trim());
        }
        return headers;
    }
}
