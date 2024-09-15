package com.commsignia.vehiclemonitorappbe.websocket;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class VehicleWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        System.out.println("--- CONNECTION ESTABLISHED: " + session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        System.out.println("--- CONNECTION CLOSED: " + session.getId() + ", Status: " + status);
        sessions.remove(session);
    }

    public void sendUpdate(String message) {
        System.out.println("--- SEND_UPDATE START");
        System.out.println("--- SEND_UPDATE sessions.size()=" + sessions.size());
        for (WebSocketSession session : sessions) {
            try {
                System.out.println("--- SEND_UPDATE: " + message);
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
