package net.gastipatis.bignestforum.security;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

// TODO: убрать подгрузку контекста спринга
@SpringBootTest
@Tag("unit")
class JwtManagerTest {

    @Autowired
    private JwtManager jwtManager;

    @Test
    void jwtManager_general() {
        UserDetails userDetails = User.builder()
                .authorities("ROLE_ADMIN")
                .username("johndoe")
                .password("12345")
                .build();
        JwtTokenPair tokenPair = jwtManager.tokensFromUserDetails(userDetails);
    }
}