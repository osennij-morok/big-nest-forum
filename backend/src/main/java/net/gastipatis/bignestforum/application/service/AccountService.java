package net.gastipatis.bignestforum.application.service;

import jakarta.validation.constraints.NotBlank;
import net.gastipatis.bignestforum.domain.account.AccountRole;
import net.gastipatis.bignestforum.dto.account.AccountDTO;

import java.util.List;

public interface AccountService {

    void changePassword(String username, @NotBlank String newPassword);

    void changeRole(Long accountId, AccountRole actorRole, AccountRole targetRole);

    void blockAccount(Long accountId, AccountRole actorRole);

    List<AccountDTO> getAll();
}
