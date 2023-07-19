package net.gastipatis.bignestforum.security;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import net.gastipatis.bignestforum.domain.account.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class UserDetailsFactoryImpl implements UserDetailsFactory {

    private static final String ROlES_PREFIX = "ROLE_";
//    private final PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public UserDetails create(String username, @Nullable List<String> roles) {
        return User.builder()
                .username(username)
                .password("{noop}") ////
//                .passwordEncoder(passwordEncoder::encode)
                .authorities(roles.stream()
                        .map(role -> ROlES_PREFIX + role)
                        .toArray(String[]::new))
                .build();
    }

    @Override
    public UserDetails create(String username, String password, List<GrantedAuthority> authorities) {
        return User.builder()
                .username(username)
//                .passwordEncoder(passwordEncoder::encode)
                .password(password)
                .authorities(authorities)
                .build();
    }

    @Override
    public UserDetails create(Account account) {
        String roleAuthorityString = ROlES_PREFIX + account.getRole().name();

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(roleAuthorityString)
                .disabled(account.getBlocked())
                .build();
    }

    @Override
    public String getRolesPrefix() {
        return ROlES_PREFIX;
    }
}
