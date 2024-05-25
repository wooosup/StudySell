package hello.hello.yju.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "item_id")

    private ItemEntity item;
    public String getOtherUserId(String googleId) {
        if (googleId.equals(seller.getGoogleId())) {
            return buyer.getGoogleId();
        } else if (googleId.equals(buyer.getGoogleId())) {
            return seller.getGoogleId();
        } else {
            throw new IllegalArgumentException("User is not part of this chat room");
        }
    }

}
