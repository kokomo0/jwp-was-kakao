package webserver;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import webserver.http.Request;
import webserver.http.Response;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private DispatcherServlet dispatcherServlet = new DispatcherServlet();

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
            Request request = new Request(IOUtils.readRequestHeader(bufferReader), "");

            String requestBody = "";
            if (!"".equals(request.get("Content-Length"))) {
                requestBody = IOUtils.readData(bufferReader, Integer.parseInt(request.get("Content-Length")));
            }
            request.setBody(requestBody);

            DataOutputStream dos = new DataOutputStream(out);
            Response response = mapRequestToResponse(request, requestBody);


            responseHeader(dos, response);
            responseBody(dos, response);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {

        }
    }

    private Response mapRequestToResponse(Request request, String requestBody) throws Exception {
        String method = request.get("method");
        String path = request.get("path");

        Controller controller = dispatcherServlet.mapController(request);
        return controller.mapRoute(request);

//        try {
//            return controllers.get("static").mapRoute(method, path, null);
//        } catch (NullPointerException e) {
//            String[] paths = path.split("/", 3);
//            String domain = paths[1]; //user
//            String subPath = paths[2].split("\\?")[0]; //create
//
//            Map<String, String> params;
//            if (request.get("Content-Type").equals("application/x-www-form-urlencoded")) {
//                params = ParsingUtils.parseQueryString(requestBody);
//            } else {
//                params = ParsingUtils.parseQueryString(path.split("\\?", 2)[1]);
//            }
//            return controllers.get(domain).mapRoute(method, subPath, params);
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//
//        }
//        return null;
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
