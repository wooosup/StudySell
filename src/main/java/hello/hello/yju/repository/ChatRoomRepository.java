package hello.hello.yju.repository;


import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findBySellerOrBuyer(UserEntity seller, UserEntity buyer);
    List<ChatRoom> findByItem(ItemEntity item);
}
