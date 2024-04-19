package hello.hello.yju.repository;

import hello.hello.yju.dto.ItemSearchDto;
import hello.hello.yju.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
