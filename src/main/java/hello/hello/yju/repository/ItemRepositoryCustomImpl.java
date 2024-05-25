package hello.hello.yju.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.hello.yju.dto.ItemSearchDto;
import hello.hello.yju.dto.MainItemDto;
import hello.hello.yju.dto.QMainItemDto;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.ItemSellStatus;
import hello.hello.yju.entity.QItemEntity;
import hello.hello.yju.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItemEntity.itemEntity.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItemEntity.itemEntity.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemName", searchBy)){
            return QItemEntity.itemEntity.itemName.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItemEntity.itemEntity.createdBy.like("%" + searchQuery + "%");
        }

        return null;
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
                                itemEntity.regTime
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