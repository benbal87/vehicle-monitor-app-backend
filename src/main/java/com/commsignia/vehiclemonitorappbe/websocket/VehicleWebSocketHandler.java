package com.commsignia.vehiclemonitorappbe.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VehicleWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        log.info("Websocket connection established. Session id={}", session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        log.info("Websocket connection closed. SessionID={} | CloseStatus={}", session.getId(), status);
        sessions.remove(session);
    }

    public void sendUpdate(String message) {
        log.debug("WebSocket sendUpdate | Number of sessions: {}", sessions.size());
        for (WebSocketSession session : sessions) {
            try {
                log.debug("WebSocket send update. SessionID={} | Message={}", session.getId(), message);
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("Failed to send WebSocket message. SessionID={} | Message={}", session.getId(), message, e);
            }
        }
    }

}
