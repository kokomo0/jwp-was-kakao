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

import static utils.ParsingUtils.parseHeader;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());


        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));
            Request request = parseHeader(IOUtils.readRequestHeader(bufferReader));

            if (!"".equals(request.get("Content-Length"))) {
                request.setBody(IOUtils.readData(bufferReader, Integer.parseInt(request.get("Content-Length"))));
            }

            DataOutputStream dos = new DataOutputStream(out);
            Response response = mapRequestToResponse(request);


            responseHeader(dos, response);
            responseBody(dos, response);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {

        }
    }

    private Response mapRequestToResponse(Request request) throws Exception {
        Controller controller = dispatcherServlet.mapController(request);
        return controller.mapRoute(request);
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
