package net.gastipatis.bignestforum.application.service;

import jakarta.validation.constraints.NotBlank;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void changePassword(String username, String newPassword) {
        String newPasswordHash = passwordEncoder.encode(newPassword);
        accountRepository.changePasswordByUsername(username, newPasswordHash);
    }
}
