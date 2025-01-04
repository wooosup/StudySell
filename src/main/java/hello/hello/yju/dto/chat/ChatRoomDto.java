package hello.hello.yju.dto.chat;

import hello.hello.yju.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomDto {
    private Long id;
    private String sellerName;
    private String buyerName;
    private String itemName;

    @Builder
    private ChatRoomDto(Long id, String sellerName, String buyerName, String itemName) {
        this.id = id;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.itemName = itemName;
    }

    public static ChatRoomDto of(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .sellerName(chatRoom.getSeller().getName())
                .buyerName(chatRoom.getBuyer().getName())
                .itemName(chatRoom.getItem().getItemName())
                .build();
    }
}
