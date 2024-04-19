package hello.hello.yju.dto;

import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    private String itemName;

    private String description;

    private Integer price;

    private Long user_id;

    private ItemSellStatus itemSellStatus;

    private static ModelMapper modelMapper = new ModelMapper();

    public ItemEntity createItem(){
        return modelMapper.map(this, ItemEntity.class);
    }

    public static ItemFormDto of(ItemEntity itemEntity) {
        return modelMapper.map(itemEntity, ItemFormDto.class);
    }

}
