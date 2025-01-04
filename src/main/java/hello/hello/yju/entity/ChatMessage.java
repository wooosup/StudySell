package hello.hello.yju.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    private String senderName;

    @Column(nullable = false)
    private String message;

    private LocalDateTime timestamp;

    @Builder
    private ChatMessage(ChatRoom chatRoom, UserEntity sender, String senderName, String message, LocalDateTime timestamp) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }
}