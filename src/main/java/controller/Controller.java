package controller;

import webserver.session.Session;
import webserver.session.SessionManager;

public interface Controller {
    String successUri = "";
    String failUri = "";

    default boolean isLoginUser(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            return false;
        }
        Session session = SessionManager.getInstance().findSession(sessionId);
        return session != null && (boolean) session.getAttribute("logined");
    }
}
