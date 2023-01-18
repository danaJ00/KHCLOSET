package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @GetMapping(value = "/mypage")
    public String myPage(Model model) {
        return "/member/mypage";
    }

    @GetMapping(value = "/user")
    public String userDtl(Principal principal, Model model){

        MemberFormDto memberFormDto = memberService.getMypageList(principal.getName());
        model.addAttribute("memberFormDto", memberFormDto);

        return "member/userForm";
    }

    @GetMapping(value = "/{memberId}")
    public String memberDtl(@PathVariable("memberId") Long memberId, Model model){
        try{
            MemberFormDto memberFormDto = memberService.getMemberDtl(memberId);
            model.addAttribute("memberFormDto", memberFormDto);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            model.addAttribute("memberFormDto", new MemberFormDto());
        }
        return "member/adminForm";
    }

    @PostMapping(value = "/{memberId}")
    public String memberUpdate(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                               Model model){
        if(bindingResult.hasErrors()){
            return "member/adminForm";
        }
        try{

            memberService.updateMember(memberFormDto, passwordEncoder);
        }catch (Exception e){
            model.addAttribute("errorMessage","회원정보 수정 중 에러가 발생하였습니다.");
            return "member/adminForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/list")
    public String memberList(Model model){
        List<Member> memberList = memberService.findMembers();
        model.addAttribute("memberList",memberList);

        return "member/memberList";
    }

    @PostMapping(value = "/user/{memberId}")
    public String memberUserUpdate(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                                   Model model){
        if(bindingResult.hasErrors()){
            return "member/userForm";
        }
        try{

            memberService.updateMember(memberFormDto, passwordEncoder);
        }catch (Exception e){
            model.addAttribute("errorMessage","회원정보 수정 중 에러가 발생하였습니다.");
            return "member/userForm";
        }

        return "redirect:/";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/delete/{membersId}")
    public String deleteMember(@PathVariable("membersId") Long memberId){
        memberService.deleteMember(memberId);
        return "redirect:/members/list";
    }

}
