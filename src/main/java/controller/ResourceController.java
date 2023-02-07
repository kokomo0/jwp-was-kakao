package controller;

import utils.FileIoUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceController implements Controller {

    private static final ResourceController instance = new ResourceController();

    private ResourceController() {
    }

    public static ResourceController getInstance() {
        return instance;
    }

    public HttpResponse handleRequest(HttpRequest httpRequest) {
        try {
            byte[] body = FileIoUtils.mapBody(httpRequest.getUri());
            return new ResponseBuilder()
                    .httpVersion(httpRequest.getHttpVersion())
                    .httpStatus(HttpStatus.OK)
                    .contentType(FileIoUtils.getContentType(httpRequest.getUri()))
                    .contentLength(String.valueOf(body.length))
                    .body(body)
                    .build();
        } catch (IOException e) {
            return new ResponseBuilder()
                    .httpStatus(HttpStatus.INTERNET_SERVER_ERROR)
                    .build();
        } catch (URISyntaxException e) {
            return new ResponseBuilder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return new ResponseBuilder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
