package hello.hello.yju.controller;



import hello.hello.yju.dto.CustomOAuth2User;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.service.ItemService;
import hello.hello.yju.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final ItemService itemService;
    private final UserService userService;

    @GetMapping("/myinfo")
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

//    @GetMapping("/myinfo")
//    public String zz(){
//        return "myinfo";
//    }
}
