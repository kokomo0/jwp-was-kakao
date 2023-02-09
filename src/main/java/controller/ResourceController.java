package controller;

import controller.annotation.RequestMapping;
import utils.FileIoUtils;
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

    @RequestMapping
    public HttpResponse viewPage(String uri) {
        //TODO: CONTENT-TYPE 정리
        try {
            byte[] body = FileIoUtils.mapBody(uri);
            return new ResponseBuilder()
                    .httpStatus(HttpStatus.OK)
                    .contentType(FileIoUtils.getContentType(uri))
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
