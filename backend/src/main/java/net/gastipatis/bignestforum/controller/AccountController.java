package net.gastipatis.bignestforum.controller;

import jakarta.validation.constraints.Positive;
import net.gastipatis.bignestforum.application.service.AccountService;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.domain.account.AccountRole;
import net.gastipatis.bignestforum.dto.account.AccountDTO;
import net.gastipatis.bignestforum.dto.account.ChangePasswordRequest;
import net.gastipatis.bignestforum.dto.account.ChangeRoleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService service;

    public AccountController(AccountRepository accountRepository, AccountService service) {
        this.accountRepository = accountRepository;
        this.service = service;
    }

    @PostMapping("{accountId}/blocking")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void blockAccount(@PathVariable @Positive Long accountId) {
        service.blockAccount(accountId, getAuthenticatedRole());
    }

    @DeleteMapping("{accountId}/blocking")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unblockAccount(@PathVariable @Positive Long accountId) {
        accountRepository.unblockAccount(accountId);
    }

    @PutMapping("{accountId}/role")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changeAccountRole(@PathVariable @Positive Long accountId, @RequestBody ChangeRoleRequest request) {
        service.changeRole(accountId, getAuthenticatedRole(), request.getRole());
    }


    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changePassword(@RequestBody ChangePasswordRequest request) {
        service.changePassword(getAuthenticatedUsername(), request.getNewPassword());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<AccountDTO> getAll() {
        return service.getAll();
    }

    private String getAuthenticatedUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }

    private AccountRole getAuthenticatedRole() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN))
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .findFirst()
                .map(authority -> authority.replaceFirst("ROLE_", ""))
                .map(AccountRole::valueOf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }
}
