package controller.support;

import controller.*;
import controller.annotation.RequestMapping;
import model.User;
import utils.FileIoUtils;
import webserver.http.Cookie;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utils.ParsingUtils.parseQueryString;

public class MethodControllerResolver {
    public HttpResponse process(Controller controller, HttpRequest httpRequest) {
        Method method = map(controller, httpRequest);
        return invoke(controller, method, httpRequest);
    }

    private Method map(Controller controller, HttpRequest httpRequest) {
        //TODO: 예외 처리 하기
//        return Arrays.stream(controller.getClass().getDeclaredMethods())
//                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
//                .filter(method -> {
//                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
//                    return support(requestMapping, httpRequest);
//                })
//                .findAny().orElseThrow();

        List<Method> l = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class)).collect(Collectors.toList());
        List<Method> l2 = l.stream().filter(method -> {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            return support(requestMapping, httpRequest);
        }).collect(Collectors.toList());

        return l2.stream().findAny().orElseThrow();
    }

    private boolean support(RequestMapping requestMapping, HttpRequest httpRequest) {
        if (!requestMapping.method().equals(httpRequest.getMethod())) return false;
        if (isResourceRequest(httpRequest)) return true;
        return requestMapping.path().equals(httpRequest.getUri().split("\\?")[0]);
    }

    private boolean isResourceRequest(HttpRequest httpRequest) {
        return httpRequest.getMethod().equals("GET") && FileIoUtils.exists(httpRequest.getUri());
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
                Parameter params = ParameterWrapper.wrap(httpRequest);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                return (HttpResponse) method.invoke(controller, user);
            }
            if (controller.equals(LoginController.getInstance())) {
                Parameter params = ParameterWrapper.wrap(httpRequest);
                params.add("uri", httpRequest.getUri());
                if (httpRequest.hasCookie())
                    params.add("JSESSIONID", Cookie.parseCookie(httpRequest.get("Cookie")).get("JSESSIONID"));
                return (HttpResponse) method.invoke(controller, params);
            }
            if (controller.equals(UserListController.getInstance())) {
                Parameter params = new Parameter();
                params.add("uri", httpRequest.getUri());
                if (httpRequest.hasCookie())
                    params.add("JSESSIONID", Cookie.parseCookie(httpRequest.get("Cookie")).get("JSESSIONID"));
                return (HttpResponse) method.invoke(controller, params);
            }
        } catch (Exception e) {
        }
        return HomeController.getInstance().badRequest();
    }

}