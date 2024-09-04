package com.example.shop2.dto.member;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
@ToString
public class MemberFormDto {
    private Long id;
    @NotBlank(message = "이름은 필수값입니다.")
    private String name;
    @NotBlank(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수값입니다.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;
    private String address;
    private String role;
    private String createdAt;
    private LocalDateTime regTime;
    private String modifiedAt;
    private LocalDateTime updateTime;
}
