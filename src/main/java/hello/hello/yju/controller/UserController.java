package hello.hello.yju.controller;



import hello.hello.yju.dto.CustomOAuth2User;
import hello.hello.yju.entity.ChatRoom;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.UserRepository;
import hello.hello.yju.service.ChatRoomService;
import hello.hello.yju.service.ItemService;
import hello.hello.yju.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;

    @GetMapping("/myinfo")
    public String myInfo() {
        return "myinfo";
    }

    @GetMapping("/myinfo/items")
    public String getMyItems(Authentication authentication, Model model) {
        if (authentication.getPrincipal() instanceof CustomOAuth2User) { // 'if' 조건문 추가
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            String googleId = customOAuth2User.getGoogleId();
            List<ItemEntity> items = userService.findItemsById(googleId);

            // 모델에 아이템 목록 추가
            model.addAttribute("items", items);
        }
        // Thymeleaf 템플릿 반환
        return "myinfo";
    }
    @GetMapping("/myinfo/rooms")
    public String chatRooms(Model model, Principal principal) {
        String username = principal.getName();
        UserEntity user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<ChatRoom> rooms = chatRoomService.findBySellerOrBuyer(user);
        model.addAttribute("rooms", rooms);
        return "myinfo";
    }

}
