package hello.hello.yju.dto;

import hello.hello.yju.entity.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;

    private String searchDepartment;

    private Long searchUserId;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

}