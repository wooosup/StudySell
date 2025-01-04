package hello.hello.yju.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/error")
public class ErrorPageController {

    @RequestMapping("/404")
    public String errorPage404() {
        log.info("errorPage404");
        return "error/404";
    }

    @RequestMapping("/500")
    public String errorPage500() {
        log.info("errorPage500");
        return "error/500";
    }
}
