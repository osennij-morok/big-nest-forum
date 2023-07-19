package net.gastipatis.bignestforum.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Username is mandatory")
    @NotNull
    private String username;

    @NotBlank(message = "Password is mandatory")
    @NotNull
    private String password;

    private String captchaToken;
}
