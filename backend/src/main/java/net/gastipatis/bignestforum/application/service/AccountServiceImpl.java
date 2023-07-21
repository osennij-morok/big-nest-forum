package net.gastipatis.bignestforum.application.service;

import net.gastipatis.bignestforum.application.exception.AccountNotExistsException;
import net.gastipatis.bignestforum.application.exception.SubordinationException;
import net.gastipatis.bignestforum.application.mapper.AccountMapper;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.domain.account.Account;
import net.gastipatis.bignestforum.domain.account.AccountRole;
import net.gastipatis.bignestforum.dto.account.AccountDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Validated
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final AccountMapper mapper;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountMapper mapper) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public void changePassword(String username, String newPassword) {
        String newPasswordHash = passwordEncoder.encode(newPassword);
        accountRepository.changePasswordByUsername(username, newPasswordHash);
    }

    @Override
    public void changeRole(Long accountId, AccountRole actorRole, AccountRole targetRole) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotExistsException();
        }
        accountRepository.changeAccountRole(targetRole, accountId);
    }

    @Override
    public void blockAccount(Long accountId, AccountRole actorRole) {
        Account targetAccount = accountRepository.findById(accountId)
                .orElseThrow(AccountNotExistsException::new);
        if (actorRole == AccountRole.OWNER) {
            accountRepository.blockAccount(accountId);
            return;
        }
        switch (targetAccount.getRole()) {
            case ADMIN, OWNER -> throw new SubordinationException();
        }
        accountRepository.blockAccount(accountId);
    }

    @Override
    public List<AccountDTO> getAll() {
        Iterable<Account> accountsIterable = accountRepository.findAll();
        return StreamSupport.stream(accountsIterable.spliterator(), false)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
