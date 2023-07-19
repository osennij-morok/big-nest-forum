package net.gastipatis.bignestforum.gateway;

public interface CaptchaGateway {

    boolean verifyToken(String clientToken);
}
