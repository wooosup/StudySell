package hello.hello.yju.service;

import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.ItemRepository;
import hello.hello.yju.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    public UserService(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public List<ItemEntity> findItemsById(String googleId) {
        UserEntity user = userRepository.findByGoogleId(googleId);
        if (user != null) {
            return itemRepository.findByUser_GoogleId(user.getGoogleId());
        } else {
            // 사용자를 찾을 수 없는 경우, 빈 목록 반환
            return Collections.emptyList();
        }
    }
}
