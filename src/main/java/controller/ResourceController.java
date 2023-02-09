package controller;

import controller.annotation.RequestMapping;
import utils.FileIoUtils;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceController implements Controller {
    private ResourceController() {
    }

    public static ResourceController getInstance() {
        return LazyHolder.it;
    }
    private static class LazyHolder {
        private static final ResourceController it = new ResourceController();
    }

    @RequestMapping
    public HttpResponse viewPage(String uri) throws IOException, URISyntaxException, NullPointerException {
        //TODO: CONTENT-TYPE 정리
        byte[] body = FileIoUtils.mapBody(uri);
        return new ResponseBuilder()
                .httpStatus(HttpStatus.OK)
                .contentType(FileIoUtils.getContentType(uri))
                .body(body)
                .build();
    }
}
