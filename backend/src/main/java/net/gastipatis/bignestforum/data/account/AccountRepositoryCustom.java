package net.gastipatis.bignestforum.data.account;

import net.gastipatis.bignestforum.domain.account.Account;

import java.util.Optional;

public interface AccountRepositoryCustom {

    Optional<Account> findAccountByUsername(String username);
}
