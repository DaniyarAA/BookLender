package kg.attractor.java.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SessionHandler {
    private static final String SESSIONS_FILE = "data/sessions.json";
    private static final Gson gson = new Gson();
    private static Map<String, String> sessions = loadSessions();

    public static Map<String, String> loadSessions() {
        try (Reader reader = new FileReader(SESSIONS_FILE)) {
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            return gson.fromJson(reader, mapType);
        } catch (IOException e) {
            System.out.println("Файл с сессиями не найден.");
            return new HashMap<>();
        }
    }

    public static void saveSessions() {
        try (Writer writer = new FileWriter(SESSIONS_FILE)) {
            gson.toJson(sessions, writer);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении сессий: " + e.getMessage());
        }
    }

    public static String getUserIdentifier(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void addSession(String sessionId, String identifier) {
        sessions.put(sessionId, identifier);
        saveSessions();
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
        saveSessions();
    }

    public static boolean containsSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}

