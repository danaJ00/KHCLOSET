package com.shop.service;

import com.shop.dto.*;
import com.shop.entity.*;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Transactional(readOnly = true)
    public MemberFormDto getMemberDtl(Long memberId){
        Member member = memberRepository.findById(memberId).get();
        List<MemberFormDto> memberFormDtoList = new ArrayList<>();

        MemberFormDto memberFormDto = MemberFormDto.of(member);
        memberFormDtoList.add(memberFormDto);

        memberFormDto.setMemberFormDtoList(memberFormDtoList);

        return memberFormDto;
    }

    @Transactional(readOnly = true)
    public MemberFormDto getMypageList(String email){
        Member member = memberRepository.findByEmail(email);
        MemberFormDto memberFormDto = MemberFormDto.of(member);

        return memberFormDto;
    }

    public void deleteMember(Long id){
        memberRepository.deleteById(id);
    }

    public long updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) throws Exception{
        Member member = memberRepository.findById(memberFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        member.updateMember(memberFormDto ,passwordEncoder);

        return member.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}