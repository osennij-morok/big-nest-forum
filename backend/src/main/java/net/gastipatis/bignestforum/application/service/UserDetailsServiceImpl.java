package net.gastipatis.bignestforum.application.service;

import lombok.AllArgsConstructor;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.domain.account.Account;
import net.gastipatis.bignestforum.security.UserDetailsFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final UserDetailsFactory userDetailsFactory;
//    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository
                .findAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with name %s does not exist", username)));
        return map(account);
    }

    private UserDetails map(Account account) {
        return userDetailsFactory.create(account);
    }
}
