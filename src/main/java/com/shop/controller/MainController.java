package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    private final MemberService memberService;

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8 );
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);


        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    @GetMapping("/itemList")
    public String itemList(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16 );
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "item/itemList";
    }

    @GetMapping(value = "/category/{itemCategory}")
    public String itemCategory(@PathVariable("itemCategory")String itemCategory, ItemSearchDto itemSearchDto,Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<MainItemDto> items = itemService.getCagtegoryItemPage(itemSearchDto,itemCategory ,pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemList";
    }

    @GetMapping(value = "/info/shopinfo")
    public String shopinfo() {
        return "info/shopinfo";
    }

    @GetMapping(value = "/info/guide")
    public String guide() {
        return "info/guide";
    }

    @GetMapping(value = "/info/privacy")
    public String privacy() {
        return "info/privacy";
    }

    @GetMapping(value = "/info/agreement")
    public String agreement() {
        return "info/agreement";
    }
}