package net.gastipatis.bignestforum.data.account;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import net.gastipatis.bignestforum.domain.account.Account;

import java.util.List;
import java.util.Optional;

public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {

    @PersistenceContext
    private EntityManager persistenceContext;

    @Override
    public Optional<Account> findAccountByUsername(String username) {
        String queryText = "from account a where a.username = :username";
        TypedQuery<Account> query = persistenceContext
                .createQuery(queryText, Account.class)
                .setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }
}
