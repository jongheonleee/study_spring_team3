package com.example.shop2.dto;

import com.example.shop2.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String address;
}
