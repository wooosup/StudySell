package hello.hello.yju.service;

import hello.hello.yju.dto.*;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.ItemImg;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ChatRoomRepository;
import hello.hello.yju.repository.ItemImgRepository;
import hello.hello.yju.repository.ItemRepository;
import hello.hello.yju.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    private final UserRepository userRepository;

    private final ChatService chatService;


    public UserEntity getCurrentUserEntityByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

    public List<ItemFormDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::convertToDtoWithRelativeTime)
                .collect(Collectors.toList());
    }

    private ItemFormDto convertToDtoWithRelativeTime(ItemEntity itemEntity) {
        ItemFormDto dto = ItemFormDto.of(itemEntity);
        dto.setRelativeTime(TimeUtils.getRelativeTime(itemEntity.getRegTime()));
        return dto;
    }


    @Transactional
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, String googleId) throws Exception {

        UserEntity userEntity = getCurrentUserEntityByGoogleId(googleId);


        //상품 등록
        ItemEntity itemEntity = itemFormDto.createItem();
        itemEntity.setRegTime(LocalDateTime.now());
        itemEntity.setUser(userEntity);
        itemRepository.save(itemEntity);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(itemEntity);

            if (i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return itemEntity.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(itemEntity);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        ItemEntity itemEntity = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        itemEntity.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return itemEntity.getId();
    }

    public void deleteItem(Long itemId) {
        // 해당 상품의 모든 이미지 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 조회된 이미지들을 모두 삭제
        if (!itemImgList.isEmpty()) {
            for (ItemImg itemImg : itemImgList) {
                itemImgRepository.delete(itemImg);
            }
        }

        // 채팅방과 메시지 삭제
        chatService.deleteChatRoomsByItemId(itemId);

        // 상품 삭제
        itemRepository.deleteById(itemId);

    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}