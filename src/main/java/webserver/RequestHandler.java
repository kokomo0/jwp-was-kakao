package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.List;

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

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, responseBody.length);
            responseBody(dos, responseBody);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private static byte[] mapRequestToResponse(List<String> requestHeader) throws IOException, URISyntaxException {
        List<String> header = IOUtils.getMethodAndPath(requestHeader.get(0));

        String method = header.get(0);
        String path = header.get(1);

        byte[] responseBody = "Hello world".getBytes();

        if (method.equals("GET") && path.equals("/index.html")) {
            responseBody = FileIoUtils.loadFileFromClasspath("templates/index.html");
        }
        return responseBody;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
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
