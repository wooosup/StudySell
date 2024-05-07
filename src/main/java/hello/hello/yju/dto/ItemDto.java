package hello.hello.yju.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String department;

    private String itemName;

    private String description;

    private Integer price;

    private String sellStatCd;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}