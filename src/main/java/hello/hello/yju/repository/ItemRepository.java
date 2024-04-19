package hello.hello.yju.repository;

import hello.hello.yju.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>,
    QuerydslPredicateExecutor<ItemEntity>, ItemRepositoryCustom {

        List<ItemEntity> findByItemName(String itemName);

        List<ItemEntity> findByItemNameOrDescription(String itemName, String description);

        List<ItemEntity> findByPriceLessThan(Integer price);

        List<ItemEntity> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from ItemEntity i where i.description like " +
            "%:description% order by i.price desc")
    List<ItemEntity> findByDescription(@Param("description") String description);

    @Query(value="select * from item i where i.description like " +
                "%:description% order by i.price desc", nativeQuery = true)
        List<ItemEntity> findByDescriptionByNative(@Param("description") String description);

    }