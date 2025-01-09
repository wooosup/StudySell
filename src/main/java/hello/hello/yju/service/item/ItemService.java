package hello.hello.yju.service.item;

import hello.hello.yju.dto.item.ItemFormDto;
import hello.hello.yju.dto.item.ItemImgDto;
import hello.hello.yju.dto.item.ItemSearchDto;
import hello.hello.yju.dto.item.MainItemDto;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.ItemImg;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.item.ItemImgRepository;
import hello.hello.yju.repository.item.ItemRepository;
import hello.hello.yju.repository.user.UserRepository;
import hello.hello.yju.service.chat.ChatService;
import hello.hello.yju.service.image.ItemImgService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;

    @Transactional
    public void saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, String googleId) throws Exception {
        UserEntity userEntity = getCurrentUserEntityByGoogleId(googleId);

        ItemEntity itemEntity = itemFormDto.toEntity();
        itemEntity.setCreateDateTime(LocalDateTime.now());
        itemEntity.assignUser(userEntity);

        itemRepository.save(itemEntity);

        saveItemImg(itemImgFileList, itemEntity);
    }

    public ItemFormDto getItemDtl(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        List<ItemImgDto> itemImgDtoList = getImgDtos(itemImgList);

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(itemEntity);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    @Transactional
    public void updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        ItemEntity itemEntity = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        itemEntity.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        updateImg(itemImgFileList, itemImgIds);
    }

    @Transactional
    public void deleteItem(Long itemId) throws IOException {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        findImgList(itemImgList);

        chatService.deleteChatRoom(itemId);
        itemRepository.deleteById(itemId);
    }

    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

    private void saveItemImg(List<MultipartFile> itemImgFileList, ItemEntity itemEntity) throws Exception {
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = ItemImg.builder()
                    .item(itemEntity)
                    .repimgYn(i == 0 ? "Y" : "N")
                    .build();

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
    }

    private void updateImg(List<MultipartFile> itemImgFileList, List<Long> itemImgIds) throws Exception {
        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
    }

    private void findImgList(List<ItemImg> itemImgList) throws IOException {
        if (!itemImgList.isEmpty()) {
            for (ItemImg itemImg : itemImgList) {
                itemImgService.deleteItemImg(itemImg);
            }
        }
    }

    private static List<ItemImgDto> getImgDtos(List<ItemImg> itemImgList) {
        return itemImgList.stream()
                .map(ItemImgDto::of)
                .toList();
    }

    private UserEntity getCurrentUserEntityByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }
}