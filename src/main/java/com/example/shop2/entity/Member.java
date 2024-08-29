package com.example.shop2.entity;

import com.example.shop2.constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    @NotEmpty
    private String email;
    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String createdAt;
    private LocalDateTime regTime;
    private String modifiedAt;
    private LocalDateTime updateTime;

//    public void updateMember(MemberFormDto memberFormDto) {
//        this.setName(memberFormDto.getName());
//        this.setEmail(memberFormDto.getEmail());
//        this.setPassword(memberFormDto.getPassword());
//        this.setAddress(memberFormDto.getAddress());
//        this.setRole(Role.USER);
//    }
//
//    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder, Role role) {
//        var member = new Member();
//
//        member.setName(memberFormDto.getName());
//        member.setEmail(memberFormDto.getEmail());
//        member.setAddress(memberFormDto.getAddress());
//        String password = passwordEncoder.encode(memberFormDto.getPassword());
//        member.setPassword(password);
//        member.setRole(role);
//
//        return member;
//    }
}
