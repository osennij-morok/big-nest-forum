package net.gastipatis.bignestforum.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.Errors;

@AllArgsConstructor
@Getter
public class EntityValidationException extends RuntimeException {

    private final Errors errors;
}
