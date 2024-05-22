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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageDto saveMessage(Long chatRoomId, String message, String senderId, String senderName) {
        // 채팅방 ID로 채팅방 엔티티 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));

        // 발신자 ID로 유저 엔티티 조회
        UserEntity sender = userRepository.findByGoogleId(senderId);
        if (sender == null) {
            throw new IllegalArgumentException("Invalid sender ID");
        }

        UserEntity sendername = userRepository.findByName(senderName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));

        // 새로운 ChatMessage 객체 생성 및 세팅
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setSenderName(sendername);
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(LocalDateTime.now());

        // 메시지 저장
        chatMessage = chatMessageRepository.save(chatMessage);

        // ChatMessageDto 생성 및 반환
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setId(chatMessage.getId());
        chatMessageDto.setChatRoomId(chatRoom.getId());
        chatMessageDto.setSenderId(sender.getGoogleId());
        chatMessageDto.setSenderName(sendername.getName());
        chatMessageDto.setMessage(chatMessage.getMessage());
        chatMessageDto.setTimestamp(chatMessage.getTimestamp());

        return chatMessageDto;
    }
}
