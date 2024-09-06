package com.example.shop2.controller.item;

import com.example.shop2.dto.item.ItemFormDto;
import com.example.shop2.service.item.FileService;
import com.example.shop2.service.item.ItemImgService;
import com.example.shop2.service.item.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/admin/item")
    public String getAdminItems(Model model) {
        return "item/adminItems";
    }

    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/registerForm";
    }

    // 3. 상품 등록 처리
    @PostMapping("/admin/item/new")
    public String addItem(@Valid ItemFormDto itemFormDto, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, BindingResult bindingResult, Model model) {
        // 1. 필드 유효성 검증
        if (bindingResult.hasErrors()) {
            return "item/registerForm";
        }

        // 2. 서비스로 상품 등록
        // 2-1. 상품 이미지 저장
        // 2-1-1. 상품 대표 이미지 없을 경우 에러 메시지 반환
        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMsg", "상품 대표 이미지가 누락되었습니다.");
            return "item/registerForm";
        }

        // 2-2. 상품 입력 데이터 저장
        // 2-2-1. 상품 입력 데이터 저장 실패시 에러 메시지 반환
        try {
            Long saveItemId = itemService.saveItem(itemFormDto, itemImgFileList);
            System.out.println("saveItemId = " + saveItemId);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "상품 등록에 실패했습니다.");
            return "item/registerForm";
        }

        // 3. 상품 관리자 페이지로 이동
        return "redirect:/admin/item";
    }

    // 4. 상품 수정 처리
    @GetMapping(value = "/admin/item/{id}")
    public String itemDetail(@PathVariable("id") Long id, Model model) {
        // 1. 전달받은 id로 상품 상세 정보 가져옴
        ItemFormDto foundItemFormDto = itemService.getItemDetail(id);

        // 2. 모델에 해당 정보를 저장
        model.addAttribute("itemFormDto", foundItemFormDto);

        // 3. 상품 등록 페이지 이동
        return "item/registerForm";
    }
}
