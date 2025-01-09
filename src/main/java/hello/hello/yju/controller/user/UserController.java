package hello.hello.yju.controller.user;


import hello.hello.yju.dto.user.CustomOAuth2User;
import hello.hello.yju.entity.ItemEntity;
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

    private final UserService userService;

    @GetMapping("/myInfo")
    public String myInfo() {
        return "myInfo";
    }

    @GetMapping("/myInfo/items")
    public String getMyItems(Authentication authentication, Model model) {
        if (authentication.getPrincipal() instanceof CustomOAuth2User customOAuth2User) {
            String googleId = customOAuth2User.getGoogleId();
            List<ItemEntity> items = userService.findItemsById(googleId);

            model.addAttribute("items", items);
        }
        return "myInfo";
    }
}
