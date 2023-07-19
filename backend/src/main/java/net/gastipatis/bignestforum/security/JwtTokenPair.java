package net.gastipatis.bignestforum.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenPair {

    private String accessToken;
    private String refreshToken;
}
