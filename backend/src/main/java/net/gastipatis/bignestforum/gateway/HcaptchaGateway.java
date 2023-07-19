package net.gastipatis.bignestforum.gateway;

import net.gastipatis.bignestforum.dto.captcha.HcaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class HcaptchaGateway implements CaptchaGateway {

    private final String hcaptchaSecret;

    private static final String VERIFICATION_URL = "https://hcaptcha.com/siteverify";

    public HcaptchaGateway(@Value("${HCAPTCHA_SECRET}") String hcaptchaSecret) {
        this.hcaptchaSecret = hcaptchaSecret;
    }

    @Override
    public boolean verifyToken(String clientToken) {
        var http = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var request = new LinkedMultiValueMap<String, String>();
        request.set("secret", hcaptchaSecret);
        request.set("response", clientToken);

        var requestEntity = new HttpEntity<MultiValueMap<String, String>>(request, headers);

        ResponseEntity<HcaptchaResponse> response = http.postForEntity(
                VERIFICATION_URL, requestEntity, HcaptchaResponse.class);
        return response.hasBody() && response.getBody().isSuccess();
    }
}
