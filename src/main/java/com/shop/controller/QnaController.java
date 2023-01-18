package com.shop.controller;

import com.shop.dto.CartDetailDto;
import com.shop.dto.QnaDto;
import com.shop.entity.Member;
import com.shop.entity.Qna;
import com.shop.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/qna")
@Controller
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    @GetMapping(value = "/new")
    public String qnaForm(Model model) {
        model.addAttribute("qnaDto", new QnaDto());

        return "qna/qnaForm";
    }

    @PostMapping(value = "/new")
    public String qnaNew(@Valid QnaDto qnaDto, BindingResult bindingResult, Principal principal,
                         Model model){
        if(bindingResult.hasErrors()){
            return "qna/qnaForm";
        }

        String email = principal.getName();
        Long qnaId;

        try{
            qnaId = qnaService.saveQna(qnaDto, email);
        } catch (Exception e){
            model.addAttribute("errorMessage","질문 등록 중 에러가 발생하였습니다.");
            return "qna/qnaForm";
        }
        return "redirect:/qna/list";
    }

    @GetMapping(value = "/list")
    public String qnaList(Principal principal, Model model) {
        List<Qna> qnaList = qnaService.qnaList(principal.getName());
        model.addAttribute("qnaList", qnaList);
        return "qna/qnaList";
    }

    @GetMapping(value = "/list/{qnaId}")
    public String qnaDtl(Model model, @PathVariable("qnaId") Long id){
        QnaDto qnaDto = qnaService.getQnaDtl(id);
        model.addAttribute("qnaDto", qnaDto);
        return "qna/qnaDtl";
    }

    @GetMapping(value = "/list/delete")
    public String qnaDelete(Long id){
        qnaService.qnaDelete(id);

        return "redirect:/qna/list";
    }

    @GetMapping(value = {"admin", "/admin/{page}"})
    public String qnaMng(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Qna> paging = this.qnaService.getqnaMng(page);
        model.addAttribute("paging", paging);
        return "qna/qnaMng";
    }


    @GetMapping(value = "/admin/delete")
    public String qnaMngDelete(Long id){
        qnaService.qnaDelete(id);

        return "redirect:/qna/admin";
    }

    @GetMapping(value = "/update/{qnaId}")
    public String qnaMngDtl(Model model, @PathVariable("qnaId") Long qnaId) {
        QnaDto qnaDto = qnaService.getQnaDtl(qnaId);
        model.addAttribute("qnaDto", qnaDto);

        return "qna/qnaAdminForm";
    }

    @PostMapping(value = "/update/{qnaId}")
    public String qnaUpdate(@Valid QnaDto qnaDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "qna/qnaAdminForm";
        }
        try{
            qnaService.updateQna(qnaDto);
        } catch (Exception e){
            model.addAttribute("errorMessage","답변 등록 중 에러가 발생하였습니다.");
            return "qna/qnaAdminForm";
        }

        return "redirect:/qna/admin";
    }

    @GetMapping(value = "/admin/list/{qnaId}")
    public String qnaAdminDtl(Model model, @PathVariable("qnaId") Long id){
        QnaDto qnaDto = qnaService.getQnaDtl(id);
        model.addAttribute("qnaDto", qnaDto);
        return "qna/qnaAdminDtl";
    }
}
