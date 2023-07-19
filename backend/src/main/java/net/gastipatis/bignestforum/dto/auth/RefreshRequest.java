package net.gastipatis.bignestforum.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshRequest {

    @NotNull
    @NotBlank(message = "Refresh token is mandatory")
    private String refreshToken;
}
