package hello.hello.yju.repository;


import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findById(Long chatRoomId);
    List<ChatRoom> findByItem_Id(Long itemId);
    Optional<ChatRoom> findBySellerAndBuyerAndItem(UserEntity seller, UserEntity buyer, ItemEntity item);
}