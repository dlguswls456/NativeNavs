// WebSocketEventListener.java
package com.nativenavs.chat.handler;

import com.nativenavs.chat.dto.UserCountDTO;
import com.nativenavs.chat.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ConnectionService connectionService;
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        int roomId = getRoomIdFromSession(headerAccessor);

        // Log the event
        System.out.println("User connected: sessionId=" + sessionId + ", roomId=" + roomId);

        // 구독이 활성화되었는지 확인하기 위해 지연 추가
        try {
            Thread.sleep(100);  // 100ms 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        connectionService.handleUserConnect(roomId, sessionId);
        sendUserCountUpdate(roomId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        Integer roomId = connectionService.getRoomIdBySessionId(sessionId);
        if (roomId != null) {
            System.out.println("User disconnected: sessionId=" + sessionId + ", roomId=" + roomId);

            connectionService.handleUserDisconnect(sessionId);
            sendUserCountUpdate(roomId);
        }
    }

    private void sendUserCountUpdate(int roomId) {
        int userCount = connectionService.getConnectedUserCount(roomId);
        messagingTemplate.convertAndSend("/status/room/" + roomId, new UserCountDTO(roomId, userCount));
    }

    private int getRoomIdFromSession(StompHeaderAccessor headerAccessor) {
        List<String> roomIdHeader = headerAccessor.getNativeHeader("roomId");
        if (roomIdHeader == null || roomIdHeader.isEmpty()) {
            throw new IllegalArgumentException("Missing roomId header in WebSocket session");
        }
        return Integer.parseInt(roomIdHeader.get(0));
    }
}
