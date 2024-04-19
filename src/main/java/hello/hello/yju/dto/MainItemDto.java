package hello.hello.yju.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String department;

    private String itemName;

    private String description;

    private String imgUrl;

    private int price;

    @QueryProjection
    public MainItemDto(Long id, String department, String itemName, String description, String imgUrl, int price )
        {
            this.id = id;
            this.department = department;
            this.itemName = itemName;
            this.description = description;
            this.imgUrl = imgUrl;
            this.price = price;
        }
}