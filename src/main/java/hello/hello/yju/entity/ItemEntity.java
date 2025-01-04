package hello.hello.yju.entity;

import hello.hello.yju.dto.item.ItemFormDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
public class ItemEntity extends BaseTimeEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department;

    private String itemName;

    private String description;

    private int price;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus = ItemSellStatus.SELL;

    @Transient
    private String relativeTime; // 상대 시간 필드

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    private ItemEntity(String department, String itemName, String description, int price, ItemSellStatus itemSellStatus, String relativeTime) {
        this.department = department;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.itemSellStatus = itemSellStatus;
        this.relativeTime = relativeTime;
    }

    public void assignUser(UserEntity user) {
        this.user = user;
    }

    public void updateItem(ItemFormDto itemFormDto){
        this.department = itemFormDto.getDepartment();
        this.itemName = itemFormDto.getItemName();
        this.description = itemFormDto.getDescription();
        this.price = itemFormDto.getPrice();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

}