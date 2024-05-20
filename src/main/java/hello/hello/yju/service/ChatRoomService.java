package hello.hello.yju.service;

import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ChatRoomRepository;
import hello.hello.yju.repository.ItemRepository;
import hello.hello.yju.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat room not found"));
    }

    @Transactional
    public ChatRoom createRoom(String sellerName, String buyerName, Long itemId) {
        UserEntity seller = userRepository.findByName(sellerName).orElseThrow(() -> new RuntimeException("Seller not found"));
        UserEntity buyer = userRepository.findByName(buyerName).orElseThrow(() -> new RuntimeException("Buyer not found"));
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(item.getItemName()+"의 채팅방");
        chatRoom.setSeller(seller);
        chatRoom.setBuyer(buyer);
        chatRoom.setItem(item);
        return chatRoomRepository.save(chatRoom);
    }

    public void deleteRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    public List<ChatRoom> findBySellerOrBuyer(UserEntity user) {
        return chatRoomRepository.findBySellerOrBuyer(user, user);
    }
}
