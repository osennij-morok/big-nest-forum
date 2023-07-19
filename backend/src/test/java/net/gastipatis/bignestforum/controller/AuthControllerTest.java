package net.gastipatis.bignestforum.controller;

import net.gastipatis.bignestforum.application.ApplicationExceptionHandler;
import net.gastipatis.bignestforum.application.validation.AuthRequestValidator;
import net.gastipatis.bignestforum.gateway.CaptchaGateway;
import net.gastipatis.bignestforum.configuration.ApplicationSecurity;
import net.gastipatis.bignestforum.controller.configuration.TestSecurityConfigurer;
import net.gastipatis.bignestforum.security.JwtManager;
import net.gastipatis.bignestforum.security.JwtTokenPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({ApplicationExceptionHandler.class})
@ContextConfiguration(classes = {AuthController.class, ApplicationSecurity.class, TestSecurityConfigurer.class})
@Tag("integration")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtManager jwtManager;
    @MockBean
    private CaptchaGateway captchaGateway;
    @MockBean
    private AuthRequestValidator requestValidator;

    @BeforeEach
    void init() {
        when(requestValidator.supports(any())).thenReturn(true);
    }

    @Test
    @WithAnonymousUser
    void given_validCredentials_expect_statusOkWithTokenPairJson() throws Exception {
        UserDetails principal = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        var tokenPair = new JwtTokenPair("/sample_access_token/", "/sample_refresh_token/");
        when(jwtManager.tokensFromUserDetails(any())).thenReturn(tokenPair);

        mockMvc.perform(post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"johndoe\", \"password\": \"123123123\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(tokenPair.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(tokenPair.getRefreshToken()))
                ;
    }

    @Test
    @WithAnonymousUser
    void given_invalidCredentials_expect_statusForbidden() throws Exception {

        UserDetails principal = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"johndoe\", \"password\": \"123123123\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}