package hello.hello.yju.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto {

    private Long id;

    private Long chatRoomId;

    private String senderId;

    private String senderName;

    private String message;

    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "ChatMessageDto{" +
                "id=" + id +
                ", chatRoomId=" + chatRoomId +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
