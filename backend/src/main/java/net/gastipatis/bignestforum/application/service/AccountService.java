package net.gastipatis.bignestforum.application.service;

import jakarta.validation.constraints.NotBlank;

public interface AccountService {

    void changePassword(String username, @NotBlank String newPassword);
}
