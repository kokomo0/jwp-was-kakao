package controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Resolver {

    public HttpResponse process(Controller controller, HttpRequest httpRequest) {
        //TODO: 예외 처리 하기
        try {
            Method method = map(controller, httpRequest);
            return (HttpResponse) method.invoke(controller, httpRequest);
        } catch (Exception e){
            return HomeController.getInstance().badRequest();
        }
    }
    private Method map(Controller controller, HttpRequest httpRequest) {
        //TODO: 예외 처리 하기
        return Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> {
                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                    return support(requestMapping, httpRequest);
                })
                .findAny().orElseThrow();
    }

    private boolean support(RequestMapping requestMapping, HttpRequest httpRequest) {
        return requestMapping.method().equals(httpRequest.getMethod()) &&
                requestMapping.path().equals(httpRequest.getUri());
    }

}
