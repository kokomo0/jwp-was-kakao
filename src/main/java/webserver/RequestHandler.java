package webserver;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;

import static utils.FileIoUtils.exists;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final HandlerMapping handlerMapping = new HandlerMapping();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = IOUtils.readRequest(new BufferedReader(new InputStreamReader(in)));
            HttpResponse httpResponse = process(httpRequest);
            IOUtils.writeResponse(new DataOutputStream(out), httpResponse);

        } catch (IOException | NumberFormatException | NullPointerException e) {
            logger.error(e.getMessage());
        }
    }

    private Controller mapController(String uri) {
        if (exists(uri)) {
            return handlerMapping.getController("resource");
        }
        return handlerMapping.getController("user");
    }

    private HttpResponse process(HttpRequest httpRequest) {
        String uri = httpRequest.getUri();
        Controller controller = mapController(uri);
        return controller.handleRequest(httpRequest);
    }

}
