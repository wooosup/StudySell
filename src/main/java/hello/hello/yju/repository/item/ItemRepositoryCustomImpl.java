package hello.hello.yju.repository.item;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.hello.yju.dto.item.ItemSearchDto;
import hello.hello.yju.dto.item.MainItemDto;
import hello.hello.yju.dto.item.QMainItemDto;
import hello.hello.yju.entity.QItemEntity;
import hello.hello.yju.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

;

public class ItemRepositoryCustomImpl implements  ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression itemNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItemEntity.itemEntity.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItemEntity itemEntity = QItemEntity.itemEntity;
        QItemImg itemImg = QItemImg.itemImg;

        // 학과에 따른 검색 조건 추가
        BooleanExpression departmentExpr = null;
        if (itemSearchDto.getSearchDepartment() != null && !itemSearchDto.getSearchDepartment().isEmpty()) {
            departmentExpr = itemEntity.department.eq(itemSearchDto.getSearchDepartment());
        }
        BooleanExpression userExpr = null;
        if (itemSearchDto.getSearchUserId() != null) {
            userExpr = itemEntity.user.googleId.eq(itemSearchDto.getSearchUserId());
        }


        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                itemEntity.id,
                                itemEntity.department,
                                itemEntity.itemName,
                                itemEntity.description,
                                itemImg.imgUrl,
                                itemEntity.price,
                                itemEntity.createDateTime
                                )
                )
                .from(itemImg)
                .join(itemImg.item, itemEntity)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .where(departmentExpr)
                .where(userExpr)
                .orderBy(itemEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, itemEntity)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .where(departmentExpr)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}