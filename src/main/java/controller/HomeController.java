package controller;

import controller.annotation.RequestMapping;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

public class HomeController implements Controller {
    private HomeController() {
    }

    public static HomeController getInstance() {
        return LazyHolder.it;
    }

    private static class LazyHolder {
        private static final HomeController it = new HomeController();
    }

    @RequestMapping
    public HttpResponse hello() {
        return new ResponseBuilder()
                .httpStatus(HttpStatus.OK)
                .contentType("text/plain")
                .body("Hello world".getBytes())
                .build();
    }

}
