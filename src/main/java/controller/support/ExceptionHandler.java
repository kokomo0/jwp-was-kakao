package controller.support;

import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

public class ExceptionHandler {
    public static HttpResponse handleException(Exception e) {
        if (e.equals(NoSuchElementException.class)) {
            return new ResponseBuilder().httpStatus(HttpStatus.NOT_FOUND).build();
        }
        if (e.equals(IOException.class)) {
            return new ResponseBuilder().httpStatus(HttpStatus.INTERNET_SERVER_ERROR).build();
        }
        if (e.equals(URISyntaxException.class)) {
            return new ResponseBuilder().httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        if (e.equals(NullPointerException.class)) {
            return new ResponseBuilder().httpStatus(HttpStatus.NOT_FOUND).build();
        }
        if (e.equals(InvocationTargetException.class)) {
            return new ResponseBuilder().httpStatus(HttpStatus.INTERNET_SERVER_ERROR).build();
        }
        if (e.equals(IllegalAccessException.class)) {
            return new ResponseBuilder().httpStatus(HttpStatus.INTERNET_SERVER_ERROR).build();
        }
        return new ResponseBuilder().httpStatus(HttpStatus.UNKNOWN_ERROR).build();
    }
}
