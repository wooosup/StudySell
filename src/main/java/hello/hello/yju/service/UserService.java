package hello.hello.yju.service;

import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.item.ItemRepository;
import hello.hello.yju.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<ItemEntity> findItemsById(String googleId) {
        UserEntity user = userRepository.findByGoogleId(googleId);
        if (user != null) {
            return itemRepository.findByUserGoogleId(user.getGoogleId());
        } else {
            return Collections.emptyList();
        }
    }
}
