package hello.hello.yju.service;

import hello.hello.yju.dto.chat.ChatMessageDto;
import hello.hello.yju.dto.chat.ChatMessageRequest;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.chat.ChatMessageRepository;
import hello.hello.yju.repository.chat.ChatRoomRepository;
import hello.hello.yju.repository.item.ItemRepository;
import hello.hello.yju.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hello.hello.yju.entity.ItemSellStatus.SELL;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatMessageServiceTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity seller;
    private UserEntity sender;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        seller = UserEntity.builder()
                .email("useop0821@gmail.com")
                .name("seller")
                .role("SELLER")
                .googleId("1234")
                .build();

        sender = UserEntity.builder()
                .email("buyer@gmail.com")
                .name("sender")
                .role("USER")
                .googleId("333")
                .build();
        userRepository.saveAll(List.of(seller, sender));

        ItemEntity item = ItemEntity.builder()
                .department("컴공")
                .itemName("운영")
                .description("설명")
                .price(1500)
                .itemSellStatus(SELL)
                .relativeTime("지금")
                .build();
        item.assignUser(seller);
        itemRepository.save(item);

        chatRoom = ChatRoom.builder()
                .seller(seller)
                .buyer(sender)
                .item(item)
                .build();
        chatRoomRepository.save(chatRoom);
        seller.getBuyer().add(chatRoom);
        userRepository.save(seller);
    }

    @Test
    void saveMessage() throws Exception {
        //given
        ChatMessageRequest request = ChatMessageRequest.builder()
                .chatRoomId(chatRoom.getId())
                .senderName(sender.getName())
                .senderId(sender.getGoogleId())
                .message("ㅎㅇㅎㅇ")
                .timestamp(null)
                .build();
        chatMessageRepository.save(request.toEntity(chatRoom, sender));

        //when
        ChatMessageDto result = chatMessageService.saveMessage(request);

        //then
        assertThat(result.getSenderName()).isEqualTo(sender.getName());
        assertThat(result.getMessage()).isEqualTo("ㅎㅇㅎㅇ");
        assertThat(result.getChatRoomId()).isEqualTo(chatRoom.getId());
    }

}