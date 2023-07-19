package net.gastipatis.bignestforum.controller.configuration;

import net.gastipatis.bignestforum.security.JwtAuthenticationFilter;
import net.gastipatis.bignestforum.security.JwtManager;
import net.gastipatis.bignestforum.security.UserDetailsFactory;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
//@Deprecated
public class TestSecurityConfigurer {

    @Mock
    private JwtManager jwtManager;
    @Mock
    private UserDetailsFactory userDetailsFactory;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtManager, userDetailsFactory);
    }
}
