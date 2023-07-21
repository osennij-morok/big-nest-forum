package net.gastipatis.bignestforum.application;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.gastipatis.bignestforum.application.exception.*;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityValidationException.class})
    protected ResponseEntity<?> handleValidation(EntityValidationException ex) {
        List<FieldError> fieldErrors = mapFieldErrors(ex.getErrors().getFieldErrors());
        return handleFieldErrors(fieldErrors);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldError> fieldErrors = mapConstraintViolationsToFieldErrors(ex.getConstraintViolations());
        return handleFieldErrors(fieldErrors);
    }

    private List<FieldError> mapConstraintViolationsToFieldErrors(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(violation -> new FieldError(
                        extractPropertyNameFromConstraintViolation(violation), violation.getMessage()))
                .collect(Collectors.toList());
    }

    private String extractPropertyNameFromConstraintViolation(ConstraintViolation<?> violation) {
        String violationStr = violation.getPropertyPath().toString();
        String[] violationTokens = violationStr.split("\\.");
        return violationTokens[violationTokens.length - 1];
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = mapFieldErrors(ex.getFieldErrors());
        return handleFieldErrors(fieldErrors);
    }

    private List<FieldError> mapFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private static ResponseEntity<Object> handleFieldErrors(List<FieldError> fieldErrors) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Invalid input")
                .withStatus(Status.UNPROCESSABLE_ENTITY)
                .with("error", Problem.builder()
                        .with("fields", fieldErrors)
                        .build())
                .build();
        return ResponseEntity.unprocessableEntity()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(ErrorResponseException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        return super.handleErrorResponseException(ex, headers, status, request);
    }

    @ExceptionHandler({ConversionFailedException.class})
    protected ResponseEntity<?> handleConversionFailure(ConversionFailedException ex) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Bad Request")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(String.format("Value %s cannot be converted to type %s",
                        ex.getValue(), ex.getTargetType()))
                .build();
        return problemResponseEntity(problem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<?> handleAuthenticationFailure(AuthenticationException ex) {
        throw ex;
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<Problem> handleAuthenticationFailure(BadCredentialsException ex) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Bad Credentials")
                .withStatus(Status.FORBIDDEN)
                .withDetail("Incorrect username or password")
                .with("code", "BAD_CREDENTIALS")
                .build();
        return problemResponseEntity(problem, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DisabledException.class})
    protected ResponseEntity<Problem> handleAuthenticationFailure(DisabledException ex) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Blocked Account")
                .withStatus(Status.FORBIDDEN)
                .withDetail("Your account is blocked")
                .with("code", "BLOCKED_ACCOUNT")
                .build();
        return problemResponseEntity(problem, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({InvalidCaptchaTokenException.class})
    protected ResponseEntity<Problem> handleInvalidCaptchaToken(InvalidCaptchaTokenException ex) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Invalid Captcha Token")
                .withStatus(Status.FORBIDDEN)
                .withDetail("The captcha token is invalid")
                .with("code", "INVALID_CAPTCHA_TOKEN")
                .build();
        return problemResponseEntity(problem, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AccountAlreadyExistsException.class})
    protected ResponseEntity<Problem> handleAccountAlreadyExists(AccountAlreadyExistsException ex) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Account Already Exists")
                .withStatus(Status.FORBIDDEN)
                .withDetail("Account with the specified credentials already exists")
                .with("code", "ACCOUNT_ALREADY_EXISTS")
                .build();
        return problemResponseEntity(problem, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AccountNotExistsException.class})
    protected ResponseEntity<Problem> handleAccountNotExists(AccountNotExistsException ex) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Account Not Exists")
                .withStatus(Status.FORBIDDEN)
                .withDetail("Account with the specified credentials do not exist")
                .with("code", "ACCOUNT_NOT_EXISTS")
                .build();
        return problemResponseEntity(problem, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({SubordinationException.class})
    protected ResponseEntity<Problem> handleSubordinationFailure(SubordinationException ex) {
        ThrowableProblem problem = Problem.builder()
                .withTitle("Subordination Violated")
                .withStatus(Status.FORBIDDEN)
                .withDetail("Cannot manage data of higher or equal rank accounts")
                .with("code", "SUBORDINATION_VIOLATED")
                .build();
        return problemResponseEntity(problem, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Problem> problemResponseEntity(Problem problem, HttpStatus status) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @AllArgsConstructor
    @Getter
    public static class FieldError {

        private String field;
        private String message;
    }
}
