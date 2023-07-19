package net.gastipatis.bignestforum.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class HcaptchaApiTest {

    private static final String SECRET_KEY = "10000000-ffff-ffff-ffff-000000000001";
    private static final String VALID_CLIENT_TOKEN = "10000000-aaaa-bbbb-cccc-000000000001";
    private static final String INVALID_CLIENT_TOKEN = "1323213123213123123";

    private CaptchaGateway captchaGateway;

    @BeforeEach
    void init() {
        captchaGateway = new HcaptchaGateway(SECRET_KEY);
    }

//    @Test
//    void given_validToken_expect_true() {
//        boolean result = captchaApi.verifyToken(VALID_CLIENT_TOKEN);
//        assertTrue(result);
//    }

    @Test
    void given_invalidToken_expect_false() {
        boolean result = captchaGateway.verifyToken(INVALID_CLIENT_TOKEN);
        assertFalse(result);
    }
}