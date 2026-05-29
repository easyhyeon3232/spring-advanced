package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)$",
        message = "비밀번호는 대문자, 숫자를 포함한 8자 이상이어야 합니다")
    @Size(min = 8)
    private String newPassword;
}
