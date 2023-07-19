package net.gastipatis.bignestforum.data;

import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.domain.account.Account;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("integration")
class AccountRepositoryCustomTest {

    @Autowired
    private AccountRepository accountRepo;

    @Test
    void when_create_then_success() {
        String username = "johndoe";
        String passwordHash = "123123123";
        int rowsInserted = accountRepo.create(username, passwordHash);
//        var createdAccount = accountRepositoryCustom.findAccountByUsername(username);
        var account = accountRepo.findAccountByUsername(username);
    }

}