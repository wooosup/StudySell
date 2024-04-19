package hello.hello.yju.service;

import hello.hello.yju.dto.ItemFormDto;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ItemRepository;
import hello.hello.yju.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {


    private final ItemRepository itemRepository;
    private final UserRepository userRepository; // UserRepository 추가

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }
    public void saveItem(ItemFormDto itemFormDto) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setItemName(itemFormDto.getItemName());
        itemEntity.setDescription(itemFormDto.getDescription());
        itemEntity.setPrice(itemFormDto.getPrice());

        itemRepository.save(itemEntity);
    }

    public List<ItemEntity> getAllItems(){
        return itemRepository.findAll();
    }
}
