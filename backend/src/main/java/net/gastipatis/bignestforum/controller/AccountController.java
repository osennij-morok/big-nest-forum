package net.gastipatis.bignestforum.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import net.gastipatis.bignestforum.application.service.AccountService;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.dto.account.ChangePasswordRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        accountRepository.blockAccount(accountId);
    }

    @DeleteMapping("{accountId}/blocking")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unblockAccount(@PathVariable @Positive Long accountId) {
        accountRepository.unblockAccount(accountId);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changePassword(@RequestBody ChangePasswordRequest request) {
        service.changePassword(getAuthenticatedUsername(), request.getNewPassword());
    }

    private String getAuthenticatedUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }
}
