package webserver;

import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import controller.Controller;
import utils.ParsingUtils;
import webserver.http.RequestHeader;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, Controller> controllers;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllers = new HashMap<>() {{
            put("user", UserController.getInstance());
        }};
    }

    /**
     * 요청을 가져와서 응답을 보내주는 역할만
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());


        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));
            RequestHeader requestHeader = new RequestHeader(IOUtils.readRequestHeader(bufferReader));
            String requestBody = IOUtils.readData(bufferReader, Integer.parseInt(requestHeader.get("Content-Length")));

            byte[] responseBody = mapRequestToResponse(requestHeader, requestBody);
            String contentType = getContentType(requestHeader.get("path"));
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, responseBody.length, contentType);
            responseBody(dos, responseBody);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(String path) {
        Map<String, String> contentTypes = new HashMap<>() {{
            put("html", "text/html");
            put("css", "text/css");
            put("js", "text/javascript");
            put("png", "image/png");
        }};
        String[] a = path.split("\\.");
        return contentTypes.getOrDefault(a[a.length > 0 ? (a.length) - 1 : 0], "text/plain");
    }

    private byte[] mapRequestToResponse(RequestHeader requestHeader, String requestBody) throws IOException, URISyntaxException {
        String method = requestHeader.get("method");
        String path = requestHeader.get("path");

        try {
            if (path.endsWith("html")) {
                return FileIoUtils.loadFileFromClasspath("templates" + path);
            }
            if (path.equals("/")) {
                return "Hello world".getBytes();
            }
            return FileIoUtils.loadFileFromClasspath("static" + path);
        } catch (NullPointerException e) {
            String[] paths = path.split("/", 3);
            String domain = paths[1]; //user
            String subPath = paths[2].split("\\?")[0]; //create

            Map<String, String> params;
            if (requestHeader.get("Content-Type").equals("application/x-www-form-urlencoded")) {
                params = ParsingUtils.parseQueryString(requestBody);
            } else {

                params = ParsingUtils.parseQueryString(path.split("\\?", 2)[1]);
            }
            return controllers.get(domain).mapRoute(method, subPath, params);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //    private byte[] mapMethod(String method, String path) throws IOException, URISyntaxException {
//
//        if (method.equals("GET")) {
//            return mapGet(path);
//        }
//
//        throw new RuntimeException();
//    }
//
//    private byte[] mapGet(String path) throws IOException, URISyntaxException {
//        try {
//            if (path.endsWith("html")) {
//                return FileIoUtils.loadFileFromClasspath("templates" + path);
//            }
//            if (path.equals("/")) {
//                return "Hello world".getBytes();
//            }
//            return FileIoUtils.loadFileFromClasspath("static" + path);
//        } catch (NullPointerException e) {
//            String[] paths = path.split("/", 3);
//            String domain = paths[1];
//            String subPath = paths[2].split("\\?")[0];
//
//
//            Map<String, String> params = ParsingUtils.parseQueryString(path);
//
//            return controllers.get(domain).mapRoute(subPath, params, );
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
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
