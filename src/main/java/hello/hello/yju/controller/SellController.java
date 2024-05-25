package hello.hello.yju.controller;

import hello.hello.yju.dto.CustomOAuth2User;
import hello.hello.yju.dto.ItemFormDto;
import hello.hello.yju.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SellController {

    private final ItemService itemService;

    @GetMapping("/user/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "upload_item";
    }

    @PostMapping("/user/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (bindingResult.hasErrors()) {
            return "upload_item";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "upload_item";
        }

        String googleId = customOAuth2User.getGoogleId();
        try {
            itemService.saveItem(itemFormDto, itemImgFileList, googleId);
        } catch (Exception e) {
            // 예외 메시지를 로그로 출력
            e.printStackTrace();
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다: " + e.getMessage());
            return "upload_item";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/user/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "upload_item";
        }
        return "upload_item";
    }

    @PostMapping(value = "/user/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "upload_item";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "upload_item";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "upload_item";
        }

        return "redirect:/";
    }

    @PostMapping("/user/item/{id}/delete")
    public String deleteItem(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItem(id);
            redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 삭제되었습니다.");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 삭제 중 오류 발생");
            return "upload_item";
        }
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "itemDtl";
    }
}
