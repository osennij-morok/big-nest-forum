package net.gastipatis.bignestforum.data.account;

import jakarta.transaction.Transactional;
import net.gastipatis.bignestforum.domain.account.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long>, AccountRepositoryCustom {

    @Modifying
    @Transactional
    @Query(
            value = "insert into account (username, password_hash) " +
                    "values (:username, :password)",
            nativeQuery = true
    )
    int create(@Param("username") String username, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("update account set blocked = true where id = :id")
    int blockAccount(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update account set blocked = false where id = :id")
    int unblockAccount(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update account set password = :newPassword where username = :username")
    int changePasswordByUsername(@Param("username") String username,
                                 @Param("newPassword") String newPassword);
}
