package controller;

import utils.FileIoUtils;
import webserver.http.Response;
import webserver.http.ResponseBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class StaticController implements Controller {

    private static StaticController instance;

    private StaticController() {
    }

    public static StaticController getInstance() {
        if (instance == null) {
            instance = new StaticController();
        }
        return instance;
    }
    public Response mapRoute(String method, String path, Map<String, String> params) throws IOException, URISyntaxException {
        try {
            ResponseBuilder responseBuilder = new ResponseBuilder();
            responseBuilder.httpStatus("200 OK")
                    .contentType(getContentType(path));

            if (path.endsWith("html")) {
                byte[] body = FileIoUtils.loadFileFromClasspath("templates" + path);
                return responseBuilder.contentType(getContentType(path)).contentLength(String.valueOf(body.length)).body(body).build();
            }
            if (path.equals("/")) {
                byte[] body = "Hello world".getBytes();
                return responseBuilder.contentLength(String.valueOf(body.length)).body(body).build();
            }
            byte[] body = FileIoUtils.loadFileFromClasspath("static" + path);
            return responseBuilder.contentType(path).contentLength(String.valueOf(body.length)).body(body).build();
        } catch(IOException | URISyntaxException e) {
            throw e;
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
}
