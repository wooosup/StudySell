package hello.hello.yju.dto;

import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.ItemSellStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    private String department;

    @NotBlank(message = "상품명을 입력해주세요.")
    private String itemName;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    private Integer price;

    private String googleId;

    private ItemSellStatus itemSellStatus;

    private String relativeTime;

    private static ModelMapper modelMapper = new ModelMapper();

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    public ItemEntity createItem(){
        return modelMapper.map(this, ItemEntity.class);
    }

    public static ItemFormDto of(ItemEntity itemEntity) {
        return modelMapper.map(itemEntity, ItemFormDto.class);
    }

}
