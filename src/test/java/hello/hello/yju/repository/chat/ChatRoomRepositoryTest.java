package hello.hello.yju.repository.chat;

import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static hello.hello.yju.entity.ItemSellStatus.SELL;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatRoomRepositoryTest {


    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("아이템 id로 채팅방 목록을 조회한다.")
    @Test
    void findByItemId() throws Exception {
        //given
        UserEntity seller = createUser("useop0821@gmail.com", "seller", "SELLER", "12345");
        UserEntity buyer = createUser("buyer@gmail.com", "buyer", "BUYER" ,"3333");

        ItemEntity item1 = createItem(seller);

        ChatRoom chatRoom1 = createChatRoom(seller, buyer, item1);
        ChatRoom chatRoom2 = createChatRoom(seller, buyer, item1);

        em.flush();
        em.clear();

        //when
        List<ChatRoom> chatRooms = chatRoomRepository.findByItemId(item1.getId());

        //then
        assertThat(chatRooms).hasSize(2)
                .extracting(ChatRoom::getId)
                .containsExactlyInAnyOrder(chatRoom1.getId(), chatRoom2.getId());
    }


    private UserEntity createUser(String email, String name, String role, String googleId) {
        UserEntity user = UserEntity.builder()
                .email(email)
                .name(name)
                .role(role)
                .googleId(googleId)
                .build();
        em.persist(user);
        return user;
    }


    private ItemEntity createItem(UserEntity user) {
        ItemEntity item = ItemEntity.builder()
                .department("컴공")
                .itemName("운영")
                .description("설명")
                .price(1500)
                .itemSellStatus(SELL)
                .relativeTime("1시간전")
                .build();
        item.assignUser(user);
        em.persist(item);
        return item;
    }

    private ChatRoom createChatRoom(UserEntity seller, UserEntity buyer, ItemEntity item) {
        ChatRoom chatRoom = ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
        em.persist(chatRoom);
        return chatRoom;
    }

}