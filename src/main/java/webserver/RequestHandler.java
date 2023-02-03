package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    /**
     * 요청을 가져와서 응답을 보내주는 역할만
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));
            List<String> requestHeader = IOUtils.readRequestHeader(bufferReader);
            byte[] responseBody = mapRequestToResponse(requestHeader);
            String contentType = getContentType(requestHeader);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, responseBody.length, contentType);
            responseBody(dos, responseBody);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(List<String> requestHeader) {
        List<String> header = IOUtils.getMethodAndPath(requestHeader.get(0));
        String path = header.get(1);
        Map<String, String> contentTypes = new HashMap<>() {{
            put("html", "text/html");
            put("css", "text/css");
            put("js", "text/javascript");
            put("png", "image/png");
        }};
        String[] a = path.split("\\.");
        return contentTypes.getOrDefault(a[a.length > 0 ? (a.length) - 1 : 0], "text/plain");
    }

    private static byte[] mapRequestToResponse(List<String> requestHeader) throws IOException, URISyntaxException {
        List<String> header = IOUtils.getMethodAndPath(requestHeader.get(0));

        String method = header.get(0);
        String path = header.get(1);

        byte[] responseBody = "Hello world".getBytes();

        responseBody = mapMethod(method, path);


        return responseBody;
    }

    private static byte[] mapMethod(String method, String path) throws IOException, URISyntaxException {
        if (method.equals("GET")) {
            return mapGet(path);
        }
        throw new RuntimeException();
    }

    private static byte[] mapGet(String path) throws IOException, URISyntaxException {
        if (path.endsWith("html")) {
            return FileIoUtils.loadFileFromClasspath("templates" + path);
        }
        if (path.equals("/")) {
            return "Hello world".getBytes();
        }
        return FileIoUtils.loadFileFromClasspath("static" + path);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
