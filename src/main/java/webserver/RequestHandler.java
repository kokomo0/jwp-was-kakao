package webserver;

import controller.support.MethodControllerResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;

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

    private HttpResponse process(HttpRequest httpRequest) {
        MethodControllerResolver resolver = new MethodControllerResolver();
        return resolver.process(handlerMapping.map(httpRequest), httpRequest);
    }

}
