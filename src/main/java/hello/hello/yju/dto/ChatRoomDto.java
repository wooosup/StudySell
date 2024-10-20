package hello.hello.yju.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDto {

    private Long id;
    private String sellerId;
    private String buyerId;
    private Long itemId ;
}
