package hello.hello.yju.repository.item;

import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.ItemImg;
import hello.hello.yju.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static hello.hello.yju.entity.ItemSellStatus.SELL;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemImgRepositoryTest {

    @Autowired
    private ItemImgRepository itemImgRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("이미지 목록을 id순으로 조회한다.")
    @Test
    void findByItemIdOrderByIdAsc() throws Exception {
        //given
        UserEntity user  = UserEntity.builder()
                .email("useop0821@gmail.com")
                .name("seop")
                .role("USER")
                .googleId("12345")
                .build();
        em.persist(user);

        ItemEntity item = ItemEntity.builder()
                .department("컴공")
                .itemName("운영체제")
                .description("설명")
                .price(5000)
                .itemSellStatus(SELL)
                .relativeTime("1시간전")
                .build();
        item.assignUser(user);
        em.persist(item);

        ItemImg img1 = ItemImg.builder()
                .imgUrl("http://test.com/img1.jpg")
                .item(item)
                .build();
        ItemImg img2 = ItemImg.builder()
                .imgUrl("http://test.com/img2.jpg")
                .item(item)
                .build();
        em.persist(img1);
        em.persist(img2);
        em.flush();

        //when
        List<ItemImg> result = itemImgRepository.findByItemIdOrderByIdAsc(item.getId());

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getImgUrl()).isEqualTo("http://test.com/img1.jpg");
        assertThat(result.get(1).getImgUrl()).isEqualTo("http://test.com/img2.jpg");
    }

    @DisplayName("존재하지 않는 아이템 id로 이미지 목록을 조회하면 빈 리스트를 반환한다.")
    @Test
    void findByItemIdOrderByIdAsc_Fail() throws Exception {
        //given
        Long itemId = 999L;

        //when
        List<ItemImg> result = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        //then
        assertThat(result).isEmpty();
    }
}