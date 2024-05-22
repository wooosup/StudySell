package hello.hello.yju.service;

import hello.hello.yju.dto.ChatMessageDto;
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
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

        ChatRoom chatRoom = chatRoomRepository.findBySellerAndBuyerAndItem(seller, buyer, item)
                .orElseGet(() -> {
                    ChatRoom newChatRoom = new ChatRoom();
                    newChatRoom.setSeller(seller);
                    newChatRoom.setBuyer(buyer);
                    newChatRoom.setItem(item);
                    return chatRoomRepository.save(newChatRoom);
                });

        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setId(chatRoom.getId());
        chatRoomDto.setSellerId(sellerId);
        chatRoomDto.setBuyerId(buyerId);
        chatRoomDto.setItemId(itemId);

        return chatRoomDto;
    }

    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));
        return chatMessageRepository.findByChatRoom(chatRoom);
    }


    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));
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