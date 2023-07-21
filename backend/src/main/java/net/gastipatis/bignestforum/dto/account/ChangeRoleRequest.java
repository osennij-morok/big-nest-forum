package net.gastipatis.bignestforum.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.gastipatis.bignestforum.domain.account.AccountRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChangeRoleRequest {

    private AccountRole role;
}
