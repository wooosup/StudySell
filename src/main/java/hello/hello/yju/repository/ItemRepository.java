package hello.hello.yju.repository;

import hello.hello.yju.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    List<ItemEntity> findByItemNm(String itemNm);

    List<ItemEntity> findByItemNmOrItemDetail(String itemNm, String itemDetail);

}
