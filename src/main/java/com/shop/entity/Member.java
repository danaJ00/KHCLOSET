package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String zipcode;

    private String streetAdr;

    private String detailAdr;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        //Member 엔티티를 수정하는 메소드
        this.name = memberFormDto.getName();
        this.email = memberFormDto.getEmail();
        this.password = passwordEncoder.encode(memberFormDto.getPassword());
        this.zipcode = memberFormDto.getZipcode();
        this.phone = memberFormDto.getPhone();
        this.streetAdr = memberFormDto.getStreetAdr();
        this.detailAdr = memberFormDto.getDetailAdr();
        this.role = memberFormDto.getRole();
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        //MemberFormDto와 PasswordEncoder를 이용하여 Member 엔티티를 생성하는 메소드를 작성
        Member member =  new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setZipcode(memberFormDto.getZipcode());
        member.setStreetAdr(memberFormDto.getStreetAdr());
        member.setDetailAdr(memberFormDto.getDetailAdr());
        member.setPhone(memberFormDto.getPhone());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);

        return member;
    }
}