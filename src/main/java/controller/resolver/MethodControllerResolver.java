package controller.resolver;

import controller.Controller;
import controller.HomeController;
import controller.ResourceController;
import controller.UserController;
import controller.annotation.RequestMapping;
import model.User;
import utils.FileIoUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static utils.ParsingUtils.parseParameter;
import static utils.ParsingUtils.parseQueryString;

public class MethodControllerResolver {
    public HttpResponse process(Controller controller, HttpRequest httpRequest) {
        Method method = map(controller, httpRequest);
        return invoke(controller, method, httpRequest);
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
        if(isResourceRequest(httpRequest)) return true;
        return requestMapping.method().equals(httpRequest.getMethod()) &&
                requestMapping.path().equals(httpRequest.getUri());
    }

    private boolean isResourceRequest(HttpRequest httpRequest) {
        return FileIoUtils.exists(httpRequest.getUri());
    }

    private HttpResponse invoke(Controller controller, Method method, HttpRequest httpRequest) {
        //TODO: 예외 처리 하기
        try {
            if (controller.equals(HomeController.getInstance()))
                return (HttpResponse) method.invoke(controller);

            if (controller.equals(ResourceController.getInstance()))
                return (HttpResponse) method.invoke(controller, httpRequest.getUri());

            if (controller.equals(UserController.getInstance())) {
                //TODO: 파싱 로직 이동할 것
                Map<String, String> params = parseQueryString(httpRequest.getBody());
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                return (HttpResponse) method.invoke(controller, user);
            }
        } catch (Exception e) {
        }
        return HomeController.getInstance().badRequest();
    }

}
