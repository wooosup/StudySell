package hello.hello.yju.service;

import hello.hello.yju.entity.ChatMessage;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.repository.ChatMessageRepository;
import hello.hello.yju.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatMessage> findAllMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Chat room not found"));
        return chatMessageRepository.findByChatRoom(chatRoom);
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }
}