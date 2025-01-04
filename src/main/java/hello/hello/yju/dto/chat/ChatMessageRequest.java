package hello.hello.yju.dto.chat;

import hello.hello.yju.entity.ChatMessage;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageRequest {

    private Long chatRoomId;
    private String senderId;
    private String senderName;
    private String message;
    private LocalDateTime timestamp;


    @Builder
    private ChatMessageRequest(Long chatRoomId, String senderId, String senderName, String message, LocalDateTime timestamp) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessage toEntity(ChatRoom room, UserEntity senderId) {
        return ChatMessage.builder()
                .chatRoom(room)
                .sender(senderId)
                .senderName(senderName)
                .message(message)
                .timestamp(timestamp)
                .build();
    }
}
