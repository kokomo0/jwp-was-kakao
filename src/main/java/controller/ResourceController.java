package controller;

import controller.annotation.RequestMapping;
import support.Parameter;
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
    public HttpResponse viewPage(Parameter parameter) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.mapBody(parameter.get("uri"));
        return new ResponseBuilder()
                .httpStatus(HttpStatus.OK)
                .contentType(FileIoUtils.getContentType(parameter.get("uri")))
                .body(body)
                .build();
    }
}
