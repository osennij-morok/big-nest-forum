package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.account.Account;
import net.gastipatis.bignestforum.dto.account.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountDTO toDto(Account entity) {
        return AccountDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .role(entity.getRole())
                .blocked(entity.getBlocked())
                .build();
    }
}
