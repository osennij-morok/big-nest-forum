package net.gastipatis.bignestforum.controller;

import net.gastipatis.bignestforum.application.ApplicationExceptionHandler;
import net.gastipatis.bignestforum.application.exception.AccountAlreadyExistsException;
import net.gastipatis.bignestforum.application.validation.SignUpRequestValidator;
import net.gastipatis.bignestforum.configuration.ApplicationSecurity;
import net.gastipatis.bignestforum.controller.configuration.TestSecurityConfigurer;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.gateway.CaptchaGateway;
import net.gastipatis.bignestforum.security.UserDetailsFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignUpController.class)
@Import({SignUpController.class, ApplicationExceptionHandler.class})
@ContextConfiguration(classes = {ApplicationSecurity.class, TestSecurityConfigurer.class})
@Tag("integration")
class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private CaptchaGateway captchaGateway;
    @MockBean
    private SignUpRequestValidator requestValidator;

    @BeforeEach
    void init() {
        when(requestValidator.supports(any())).thenReturn(true);
    }

    @Test
    @WithAnonymousUser
    void given_nonExistingUserCredentials_expect_statusOk() throws Exception {

        String passwordHash = "$argon2id$v=19$m=16384,t=2,p=1$uTqiOyYXl9u5fTnYxBO/pw$U7uiXSCHhYAl+q/JqTkNKXUIsHkqoiMWabkp++9o5DM";
        when(passwordEncoder.encode("1234")).thenReturn(passwordHash);
        when(accountRepository.create(eq("johndoe"), eq(passwordHash))).thenReturn(1);

        mockMvc.perform(post("/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"johndoe\", \"password\": \"1234\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void given_existingUserCredentials_expect_statusForbidden() throws Exception {

        String passwordHash = "$argon2id$v=19$m=16384,t=2,p=1$uTqiOyYXl9u5fTnYxBO/pw$U7uiXSCHhYAl+q/JqTkNKXUIsHkqoiMWabkp++9o5DM";
        String rawPassword = "1234";
        when(passwordEncoder.encode(rawPassword)).thenReturn(passwordHash);
        when(accountRepository.create(eq("johndoe"), eq(passwordHash))).thenThrow(AccountAlreadyExistsException.class);

        mockMvc.perform(post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"johndoe\", \"password\": \"1234\" }"))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}