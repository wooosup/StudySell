package hello.hello.yju.service;

import hello.hello.yju.dto.chat.ChatRoomDto;
import hello.hello.yju.dto.chat.ChatRoomRequest;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.chat.ChatMessageRepository;
import hello.hello.yju.repository.chat.ChatRoomRepository;
import hello.hello.yju.repository.item.ItemRepository;
import hello.hello.yju.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static hello.hello.yju.entity.ItemSellStatus.SELL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ChatService chatService;

    private UserEntity seller;
    private UserEntity buyer;
    private ItemEntity item;
    private ChatRoomRequest request;

    @BeforeEach
    void setUp() {
        seller = UserEntity.builder()
                .email("useop0821@gmail.com")
                .name("seller")
                .role("SELLER")
                .googleId("1234")
                .build();

        buyer = UserEntity.builder()
                .email("buyer@gmail.com")
                .name("buyer")
                .role("BUYER")
                .googleId("333")
                .build();

        item = ItemEntity.builder()
                .department("컴공")
                .itemName("운영")
                .description("설명")
                .price(1500)
                .itemSellStatus(SELL)
                .relativeTime("지금")
                .build();
        item.assignUser(seller);
        ReflectionTestUtils.setField(item, "id", 1L);

        request = ChatRoomRequest.builder()
                .itemId(item.getId())
                .buyerId(buyer.getGoogleId())
                .build();
    }

    @DisplayName("채팅방을 생성한다.")
    @Test
    void createChatRoom() throws Exception {
        //given
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findByGoogleId(request.getBuyerId())).thenReturn(buyer);
        when(chatRoomRepository.findBySellerAndBuyerAndItem(seller, buyer, item)).thenReturn(Optional.empty());

        ChatRoom chatRoom = ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
        ReflectionTestUtils.setField(chatRoom, "id", 1L);

        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);

        //when
        ChatRoomDto result = chatService.createChatRoom(request);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(chatRoom.getId());
        assertThat(result.getSellerName()).isEqualTo(seller.getName());
        assertThat(result.getBuyerName()).isEqualTo(buyer.getName());
        assertThat(result.getItemName()).isEqualTo(item.getItemName());

        verify(itemRepository, times(1)).findById(item.getId());
        verify(userRepository, times(1)).findByGoogleId(request.getBuyerId());
        verify(chatRoomRepository, times(1)).findBySellerAndBuyerAndItem(seller, buyer, item);
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @DisplayName("이미 존재하는 채팅방이 있을 경우 기존 채팅방을 반환한다.")
    @Test
    void existingChatRoom() throws Exception {
        //given
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findByGoogleId(request.getBuyerId())).thenReturn(buyer);
        ChatRoom existingChatRoom = ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
        when(chatRoomRepository.findBySellerAndBuyerAndItem(seller, buyer, item))
                .thenReturn(Optional.of(existingChatRoom));
        ReflectionTestUtils.setField(existingChatRoom, "id", 1L);

        //when
        ChatRoomDto result = chatService.createChatRoom(request);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingChatRoom.getId());

        verify(itemRepository, times(1)).findById(item.getId());
        verify(userRepository, times(1)).findByGoogleId(request.getBuyerId());
        verify(chatRoomRepository, times(1)).findBySellerAndBuyerAndItem(seller, buyer, item);
        verify(chatRoomRepository, never()).save(any(ChatRoom.class));
    }

    @DisplayName("아이템을 찾지 못하면 예외를 던진다.")
    @Test
    void itemNotFound() throws Exception {
        //given
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        //expect
        assertThatThrownBy(() -> chatService.createChatRoom(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품을 찾지 못했습니다.");
    }

    @DisplayName("구매자를 찾지 못하면 예외를 던진다.")
    @Test
    void buyerNotFound() throws Exception {
        //given
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findByGoogleId(request.getBuyerId())).thenReturn(null);

        //expect
        assertThatThrownBy(() -> chatService.createChatRoom(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구매자를 찾지 못했습니다.");
    }

    @DisplayName("존재하지 않는 채팅방을 조회하면 예외를 던진다.")
    @Test
    void chatRoomNotFound() throws Exception {
        //given
        Long chatRoomId = 1L;
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.empty());

        //expect
        assertThatThrownBy(() -> chatService.getChatRoom(chatRoomId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("채팅방을 찾지 못했습니다.");
    }

    @DisplayName("사용자의 채팅방 목록을 조회한다.")
    @Test
    void getUserChatRooms() throws Exception {
        //given
        String googleId = seller.getGoogleId();
        when(userRepository.findByGoogleId(googleId)).thenReturn(seller);

        ChatRoom chatRoom1 = ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
        ReflectionTestUtils.setField(chatRoom1, "id", 1L);

        ChatRoom chatRoom2 = ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
        ReflectionTestUtils.setField(chatRoom2, "id", 2L);

        List<ChatRoom> chatRooms = Arrays.asList(chatRoom1, chatRoom2);
        when(chatRoomRepository.findBySellerOrBuyer(seller)).thenReturn(chatRooms);

        //when
        List<ChatRoomDto> result = chatService.getUserChatRooms(googleId);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(chatRoom1.getId());
        assertThat(result.get(1).getId()).isEqualTo(chatRoom2.getId());

        verify(userRepository, times(1)).findByGoogleId(googleId);
        verify(chatRoomRepository, times(1)).findBySellerOrBuyer(seller);
    }

    @DisplayName("채팅방을 삭제한다.")
    @Test
    void deleteChatRoom() throws Exception {
        //given
        Long itemId = item.getId();
        ChatRoom chatRoom1 = ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
        ReflectionTestUtils.setField(chatRoom1, "id", 1L);

        ChatRoom chatRoom2 = ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
        ReflectionTestUtils.setField(chatRoom2, "id", 2L);

        List<ChatRoom> chatRooms = Arrays.asList(chatRoom1, chatRoom2);
        when(chatRoomRepository.findByItem_Id(itemId)).thenReturn(chatRooms);

        //when
        chatService.deleteChatRoom(itemId);

        //then
        verify(chatRoomRepository, times(1)).findByItem_Id(itemId);
        verify(chatMessageRepository, times(2)).deleteByChatRoom(any(ChatRoom.class));
        verify(chatRoomRepository, times(2)).delete(any(ChatRoom.class));
    }
}