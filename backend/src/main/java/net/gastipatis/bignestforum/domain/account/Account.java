package net.gastipatis.bignestforum.domain.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.gastipatis.bignestforum.converter.AccountRoleConverter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "blocked", nullable = false)
    private Boolean blocked;

    @Enumerated(EnumType.STRING)
    @Convert(converter = AccountRoleConverter.class)
    @Column(name = "role", nullable = false)
    private AccountRole role;
}
