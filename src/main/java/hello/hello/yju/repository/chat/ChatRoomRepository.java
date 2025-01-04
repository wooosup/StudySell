package hello.hello.yju.repository.chat;


import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findById(Long chatRoomId);
    List<ChatRoom> findByItem_Id(Long itemId);

    @Query("SELECT c FROM ChatRoom c WHERE c.seller = :user OR c.buyer = :user")
    List<ChatRoom> findBySellerOrBuyer(@Param("user") UserEntity user);

    Optional<ChatRoom> findBySellerAndBuyerAndItem(UserEntity seller, UserEntity buyer, ItemEntity item);
}