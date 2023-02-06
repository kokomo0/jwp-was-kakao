package webserver;

import controller.StaticController;
import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import controller.Controller;
import utils.ParsingUtils;
import webserver.http.RequestHeader;
import webserver.http.Response;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
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
            put("static", StaticController.getInstance());
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

            String requestBody = "";
            if (!"".equals(requestHeader.get("Content-Length"))) {
                requestBody = IOUtils.readData(bufferReader, Integer.parseInt(requestHeader.get("Content-Length")));
            }

            DataOutputStream dos = new DataOutputStream(out);
            Response response = mapRequestToResponse(requestHeader, requestBody);


            responseHeader(dos, response);
            responseBody(dos, response);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {

        }
    }

    private Response mapRequestToResponse(RequestHeader requestHeader, String requestBody) throws Exception {
        String method = requestHeader.get("method");
        String path = requestHeader.get("path");

        try {
            return controllers.get("static").mapRoute(method, path, null);
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
        } catch (Exception e) {

        }
        return null;
    }

    //    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8 \r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }

    private void responseHeader(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getStatusLine() + " \r\n");
            dos.writeBytes(response.getHeaders());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, Response response) {
        try {
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
