package net.gastipatis.bignestforum.security;

import net.gastipatis.bignestforum.domain.account.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserDetailsFactory {

    UserDetails create(String username, List<String> roles);

    UserDetails create(String username, String password, List<GrantedAuthority> authorities);

    UserDetails create(Account account);

    String getRolesPrefix();
}
