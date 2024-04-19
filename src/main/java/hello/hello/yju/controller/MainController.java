package hello.hello.yju.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @GetMapping("/user/info")
    public String myInfo(){
        return "myinfo";
    }


    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

}
