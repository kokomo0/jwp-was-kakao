package controller;

import webserver.session.Session;
import webserver.session.SessionManager;

public interface Controller {
    default boolean isLoginUser(String sessionId) {
        if (sessionId.isEmpty() || sessionId == null) {
            return false;
        }
        Session session = SessionManager.getInstance().findSession(sessionId);
        return session != null && (boolean) session.getAttribute("logined");
    }
}
