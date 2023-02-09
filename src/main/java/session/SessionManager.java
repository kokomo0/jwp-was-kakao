package session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private final Map<String, Session> sessions = new HashMap<>();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        add(new Session(sessionId));
        return sessionId;
    }

    public void add(final Session session) {
        sessions.put(session.getId(), session);
    }

    public Session findSession(final String id) {
        return sessions.getOrDefault(id, null);
    }

    public void remove(final String id) {
        sessions.remove(id);
    }

}