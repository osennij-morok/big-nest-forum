package net.gastipatis.bignestforum.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.gastipatis.bignestforum.domain.account.AccountRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private Long id;
    private String username;
    private Boolean blocked;
    private AccountRole role;
}
