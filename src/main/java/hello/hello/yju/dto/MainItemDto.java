package hello.hello.yju.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long item_id;

    private String itemName;

    private String description;

    private String imgUrl;

    private int price;

    @QueryProjection
    public MainItemDto(Long item_id, String itemName, String description, String imgUrl, int price )
        {
            this.item_id = item_id;
            this.itemName = itemName;
            this.description = description;
            this.imgUrl = imgUrl;
            this.price = price;
        }
}