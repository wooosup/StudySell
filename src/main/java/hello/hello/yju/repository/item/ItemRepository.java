package hello.hello.yju.repository.item;

import hello.hello.yju.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>, QuerydslPredicateExecutor<ItemEntity>, ItemRepositoryCustom {

        List<ItemEntity> findByUserGoogleId(String googleId);
}