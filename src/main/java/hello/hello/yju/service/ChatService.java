package hello.hello.yju.service;

import hello.hello.yju.dto.ChatRoomDto;
import hello.hello.yju.entity.ChatMessage;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ChatMessageRepository;
import hello.hello.yju.repository.ChatRoomRepository;
import hello.hello.yju.repository.ItemRepository;
import hello.hello.yju.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ChatRoomDto createChatRoom(String sellerId, String buyerId, Long itemId) {
        UserEntity seller = userRepository.findByGoogleId(sellerId);
        UserEntity buyer = userRepository.findByGoogleId(buyerId);
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾지 못했습니다."));

        // 동일한 채팅방이 이미 존재하는지 확인
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findBySellerAndBuyerAndItem(seller, buyer, item);
        if (existingChatRoom.isPresent()) {
            ChatRoom chatRoom = existingChatRoom.get();
            return convertToDto(chatRoom);
        }

        // 채팅방이 존재하지 않는 경우 새로운 채팅방 생성
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setSeller(seller);
        newChatRoom.setBuyer(buyer);
        newChatRoom.setItem(item);
        ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);

        return convertToDto(savedChatRoom);
    }

    private ChatRoomDto convertToDto(ChatRoom chatRoom) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setId(chatRoom.getId());
        chatRoomDto.setSellerId(chatRoom.getSeller().getGoogleId());
        chatRoomDto.setBuyerId(chatRoom.getBuyer().getGoogleId());
        chatRoomDto.setItemId(chatRoom.getItem().getId());
        return chatRoomDto;
    }

    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못했습니다."));
        return chatMessageRepository.findByChatRoom(chatRoom);
    }

    public List<ChatRoom> getUserChatRooms(String userId) {
        UserEntity user = userRepository.findByGoogleId(userId);
        return chatRoomRepository.findBySellerOrBuyer(user);
    }

    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못했습니다."));
    }

    public UserEntity getUser(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

    public void deleteChatRoomsByItemId(Long itemId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByItem_Id(itemId);
        for (ChatRoom chatRoom : chatRooms) {
            chatMessageRepository.deleteByChatRoom(chatRoom);
            chatRoomRepository.delete(chatRoom);
        }
    }
}
