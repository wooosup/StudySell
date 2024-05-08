package hello.hello.yju.entity;

import hello.hello.yju.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "item")
public class ItemEntity extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department;

    private String itemName;

    private String description;

    private int price;

    private ItemSellStatus itemSellStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void updateItem(ItemFormDto itemFormDto){
        this.department = itemFormDto.getDepartment();
        this.itemName = itemFormDto.getItemName();
        this.description = itemFormDto.getDescription();
        this.price = itemFormDto.getPrice();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

}