package support;

import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.ResponseBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

public class ExceptionHandler {
    public static HttpResponse handleException(Exception e) {
        if (e instanceof InvocationTargetException) {
            return handleException((Exception) ((InvocationTargetException) e).getTargetException());
        }
        if (e instanceof NoSuchElementException) {
            return new ResponseBuilder().httpStatus(HttpStatus.NOT_FOUND).build();
        }
        if (e instanceof NullPointerException) {
            return new ResponseBuilder().httpStatus(HttpStatus.NOT_FOUND).build();
        }
        if (e instanceof IOException) {
            return new ResponseBuilder().httpStatus(HttpStatus.INTERNET_SERVER_ERROR).build();
        }
        if (e instanceof URISyntaxException) {
            return new ResponseBuilder().httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        if (e instanceof IllegalAccessException) {
            return new ResponseBuilder().httpStatus(HttpStatus.INTERNET_SERVER_ERROR).build();
        }
        if (e instanceof IllegalArgumentException) {
            return new ResponseBuilder().httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseBuilder().httpStatus(HttpStatus.UNKNOWN_ERROR).build();
    }
}
