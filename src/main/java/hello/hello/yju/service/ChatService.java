package hello.hello.yju.service;

import hello.hello.yju.dto.chat.ChatRoomDto;
import hello.hello.yju.dto.chat.ChatRoomRequest;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ChatMessageRepository;
import hello.hello.yju.repository.ChatRoomRepository;
import hello.hello.yju.repository.ItemRepository;
import hello.hello.yju.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ChatRoomDto createChatRoom(ChatRoomRequest request) {
        ItemEntity item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못했습니다."));
        UserEntity seller = item.getUser();
        UserEntity buyer = findBuyer(request);

        // 동일한 채팅방이 이미 존재하는지 확인
        return existingChatRoom(request, seller, buyer, item);
    }


    public List<ChatRoomDto> getUserChatRooms(String userId) {
        UserEntity user = userRepository.findByGoogleId(userId);
        return chatRoomRepository.findBySellerOrBuyer(user).stream()
                .map(ChatRoomDto::of)
                .toList();
    }

    public ChatRoomDto getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .map(ChatRoomDto::of)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못했습니다."));
    }

    @Transactional
    public void deleteChatRoom(Long itemId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByItem_Id(itemId);
        deleteChatRooms(chatRooms);
    }

    private UserEntity findBuyer(ChatRoomRequest request) {
        UserEntity buyer = userRepository.findByGoogleId(request.getBuyerId());
        if (buyer == null) {
            throw new IllegalArgumentException("구매자를 찾지 못했습니다.");
        }
        return buyer;
    }

    private void deleteChatRooms(List<ChatRoom> chatRooms) {
        for (ChatRoom chatRoom : chatRooms) {
            chatMessageRepository.deleteByChatRoom(chatRoom);
            chatRoomRepository.delete(chatRoom);
        }
    }

    private ChatRoomDto existingChatRoom(ChatRoomRequest request, UserEntity seller, UserEntity buyer, ItemEntity item) {
        return chatRoomRepository.findBySellerAndBuyerAndItem(seller, buyer, item)
                .map(ChatRoomDto::of)
                .orElseGet(() -> {
                    ChatRoom newChatRoom = request.toEntity(seller, buyer, item);
                    ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
                    return ChatRoomDto.of(savedChatRoom);
                });
    }
}
