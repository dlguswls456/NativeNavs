package com.nativenavs.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

    @Column(name = "tour_id", nullable = false)
    private int tourId;

    @Column(name = "sender_id", nullable = false)
    private int senderId;

    @Column(name = "sender_nickname", nullable = false)
    private String senderNickname;

    @Column(name = "sender_is_nav", nullable = false)
    private boolean senderIsNav;

    @Column(name = "receiver_id", nullable = false)
    private int receiverId;

    @Column(name = "receiver_nickname", nullable = false)
    private String receiverNickname;

    @Column(name = "receiver_is_nav", nullable = false)
    private boolean receiverIsNav;

    @Column(name = "recent_message_content")
    private String recentMessageContent;

    @Column(name = "recent_message_time")
    private long recentMessageTime;

    /**
     * 채팅방 생성
     * @param tourId 투어 ID
     * @param senderId 발신자 ID
     * @param senderNickname 발신자 닉네임
     * @param senderIsNav 발신자 NAV 여부
     * @param receiverId 수신자 ID
     * @param receiverNickname 수신자 닉네임
     * @param receiverIsNav 수신자 NAV 여부
     * @return Room Entity
     */
    public static RoomEntity createRoom(int tourId, int senderId, String senderNickname, boolean senderIsNav, int receiverId, String receiverNickname, boolean receiverIsNav) {
        return RoomEntity.builder()
                .tourId(tourId)
                .senderId(senderId)
                .senderNickname(senderNickname)
                .senderIsNav(senderIsNav)
                .receiverId(receiverId)
                .receiverNickname(receiverNickname)
                .receiverIsNav(receiverIsNav)
                .recentMessageContent(null)
                .recentMessageTime(0L)
                .build();
    }
}
