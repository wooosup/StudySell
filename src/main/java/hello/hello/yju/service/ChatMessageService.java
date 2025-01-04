package hello.hello.yju.service;

import hello.hello.yju.dto.chat.ChatMessageDto;
import hello.hello.yju.dto.chat.ChatMessageRequest;
import hello.hello.yju.entity.ChatMessage;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ChatMessageRepository;
import hello.hello.yju.repository.ChatRoomRepository;
import hello.hello.yju.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageDto saveMessage(ChatMessageRequest request) {
        // 채팅방 검증 및 조회
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못했습니다."));

        // 발신자 검증 및 조회
        UserEntity sender = findSender(request);

        setTime(request);

        ChatMessage chatMessage = request.toEntity(chatRoom, sender);
        chatMessage = chatMessageRepository.save(chatMessage);

        return ChatMessageDto.of(chatMessage);
    }

    public List<ChatMessageDto> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못했습니다."));
        return chatMessageRepository.findByChatRoom(chatRoom).stream()
                .map(ChatMessageDto::of)
                .toList();
    }

    private static void setTime(ChatMessageRequest request) {
        if (request.getTimestamp() == null) {
            request.setTimestamp(LocalDateTime.now());
        }
    }

    private UserEntity findSender(ChatMessageRequest request) {
        UserEntity sender = userRepository.findByGoogleId(request.getSenderId());
        if (sender == null) {
            throw new IllegalArgumentException("발신자를 찾지 못했습니다.");
        }
        return sender;
    }

}
