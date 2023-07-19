package net.gastipatis.bignestforum.dto.captcha;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class HcaptchaVerificationRequest {

    private String secret;
    private String response;
}
