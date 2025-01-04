package hello.hello.yju.dto.item;

import com.querydsl.core.annotations.QueryProjection;
import hello.hello.yju.dto.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String department;

    private String itemName;

    private String description;

    private String imgUrl;

    private int price;

    private String relativeTime;

    @QueryProjection
    public MainItemDto(Long id, String department, String itemName, String description, String imgUrl, int price, LocalDateTime regTime)
        {
            this.id = id;
            this.department = department;
            this.itemName = itemName;
            this.description = description;
            this.imgUrl = imgUrl;
            this.price = price;
            this.relativeTime = TimeUtils.getRelativeTime(regTime);
        }
}