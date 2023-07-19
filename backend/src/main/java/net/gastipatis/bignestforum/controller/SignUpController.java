package net.gastipatis.bignestforum.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.gastipatis.bignestforum.application.exception.AccountAlreadyExistsException;
import net.gastipatis.bignestforum.application.exception.EntityValidationException;
import net.gastipatis.bignestforum.application.exception.InvalidCaptchaTokenException;
import net.gastipatis.bignestforum.application.validation.SignUpRequestValidator;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.dto.auth.SignUpRequest;
import net.gastipatis.bignestforum.gateway.CaptchaGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*",
//             allowedHeaders = "*",
//             methods = {
//                 RequestMethod.POST,
//                 RequestMethod.GET,
//                 RequestMethod.PUT,
//                 RequestMethod.DELETE,
//                 RequestMethod.HEAD,
//                 RequestMethod.OPTIONS
//             })
@Tag(name = "Registration")
@RestController
@RequestMapping
public class SignUpController {

    @Value("#{new Boolean('${captcha.isRequired}')}")
    private boolean requireCaptcha;

    private final AccountRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final CaptchaGateway captchaGateway;
    private final SignUpRequestValidator validator;

    public SignUpController(AccountRepository repo, PasswordEncoder passwordEncoder, CaptchaGateway captchaGateway, SignUpRequestValidator validator) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.captchaGateway = captchaGateway;
        this.validator = validator;
    }

    // TODO: move repo access into the service
    @PostMapping("signUp")
    void signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        Errors errors = new BeanPropertyBindingResult(signUpRequest, "signUpRequest");
        ValidationUtils.invokeValidator(validator, signUpRequest, errors);
        if (errors.hasErrors()) {
            throw new EntityValidationException(errors);
        }

        if (requireCaptcha) {
            boolean captchaIsInvalid = !captchaGateway.verifyToken(signUpRequest.getCaptchaToken());
            if (captchaIsInvalid) {
                throw new InvalidCaptchaTokenException();
            }
        }

        String passwordHash = passwordEncoder.encode(signUpRequest.getPassword());
        try {
            repo.create(signUpRequest.getUsername(), passwordHash);
        } catch (DuplicateKeyException e) {
            System.out.println();
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new AccountAlreadyExistsException(e);
        } catch (DataAccessException e) {
            System.out.println();
            throw e;
        } catch (Exception e) {
            System.out.println();
            throw e;
        }
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    APIError handleDataIntegrityViolation(DataIntegrityViolationException ex) {
//        return new APIError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
//    }
}
