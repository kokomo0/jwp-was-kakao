package utils;

import webserver.http.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static utils.ParsingUtils.parseHeader;

public class IOUtils {
    /**
     * @param br: Request Body를 시작하는 시점이어야
     * @param contentLength:  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException: bufferReader.read() 에서 발생할 수 있는 IO 예외
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    /**
     * @throws IOException: bufferReader.readline() 에서 발생할 수 있는 IO 예외
     */
    public static List<String> readRequestHeader(BufferedReader br) throws IOException {
        List<String> requestHeader = new ArrayList<>();
        String line = br.readLine();
        while (!"".equals(line) || line == null) {
            requestHeader.add(line);
            line = br.readLine();
        }
        return requestHeader;
    }

    /**
     * @throws IOException: bufferReader.read(), readeline()에서 발생할 수 있는 IO 예외
     * @throws NumberFormatException: Content-Length의 값이 정수가 아닐 경우 발생할 수 있는 예외
     */
    public static Request readRequest(BufferedReader br) throws IOException, NumberFormatException {
        List<String> rawHeader = readRequestHeader(br);
        String[] requestLine = rawHeader.get(0).split(" ");
        Map<String, String> headers = parseHeader(rawHeader);

        if (!"".equals(headers.getOrDefault("Content-Length", "")))
            return new Request(requestLine[0], requestLine[1], requestLine[2], headers, readData(br, Integer.parseInt(headers.get("Content-Length"))));
        return new Request(requestLine[0], requestLine[1], requestLine[2], headers, "");
    }
}
