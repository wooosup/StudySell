package hello.hello.yju.service;

import hello.hello.yju.dto.ChatMessageDto;
import hello.hello.yju.entity.ChatMessage;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ChatMessageRepository;
import hello.hello.yju.repository.ChatRoomRepository;
import hello.hello.yju.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    @Transactional
    public ChatMessageDto saveMessage(Long chatRoomId, String message, String senderId, String senderName) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));

        UserEntity sender = userRepository.findByGoogleId(senderId);
        if (sender == null) {
            throw new IllegalArgumentException("Invalid sender ID");
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setSenderName(senderName);
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(LocalDateTime.now());

        chatMessage = chatMessageRepository.save(chatMessage);

        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setId(chatMessage.getId());
        chatMessageDto.setChatRoomId(chatMessage.getChatRoom().getId());
        chatMessageDto.setSenderId(sender.getGoogleId());
        chatMessageDto.setSenderName(chatMessage.getSenderName());
        chatMessageDto.setMessage(chatMessage.getMessage());
        chatMessageDto.setTimestamp(chatMessage.getTimestamp());

        // 알림 전송
        String recipientId = chatRoom.getOtherUserId(sender.getGoogleId());
        alarmService.alarmByMessage(recipientId, chatMessageDto);


        return chatMessageDto;
    }
}
