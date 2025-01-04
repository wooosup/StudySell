package hello.hello.yju.dto.chat;

import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequest {
    private String sellerId;
    private String buyerId;
    private Long itemId;

    @Builder
    private ChatRoomRequest(String sellerId, String buyerId, Long itemId) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.itemId = itemId;
    }

    public ChatRoom toEntity(UserEntity sellerId, UserEntity buyerId, ItemEntity itemId) {
        return ChatRoom.builder()
                .seller(sellerId)
                .buyer(buyerId)
                .item(itemId)
                .build();
    }
}
