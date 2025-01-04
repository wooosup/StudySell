package hello.hello.yju.dto.chat;

import hello.hello.yju.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageDto {

    private final Long id;
    private final Long chatRoomId;
    private final String senderId;
    private final String senderName;
    private final String message;
    private final LocalDateTime timestamp;

    @Builder
    private ChatMessageDto(Long id, Long chatRoomId, String senderId, String senderName, String message, LocalDateTime timestamp) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ChatMessageDto of(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getGoogleId())
                .senderName(chatMessage.getSender().getName())
                .message(chatMessage.getMessage())
                .timestamp(chatMessage.getTimestamp())
                .build();
    }
}
