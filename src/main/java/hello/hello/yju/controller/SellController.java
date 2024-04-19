package hello.hello.yju.controller;

import hello.hello.yju.dto.ItemFormDto;
import hello.hello.yju.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class SellController {

    private final ItemService itemService;

    // ItemService를 주입받는 생성자
    public SellController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 상품 등록 페이지를 보여주는 메소드
    @GetMapping("/upload_item")
    public String newItemForm(Model model) {
        model.addAttribute("itemForm", new ItemFormDto());
        return "upload_item";
    }
    // 상품 등록 요청을 처리하는 메소드
    @PostMapping("/upload_item")
    public ModelAndView uploadItem(@ModelAttribute ItemFormDto itemFormDto) {
        itemService.saveItem(itemFormDto);
        return new ModelAndView("redirect:/"); // 성공적으로 업로드 후 리다이렉트 될 페이지
    }


}