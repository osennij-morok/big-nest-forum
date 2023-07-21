package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.account.Account;
import net.gastipatis.bignestforum.dto.account.AccountDTO;

public interface AccountMapper {

    AccountDTO toDto(Account entity);
}
