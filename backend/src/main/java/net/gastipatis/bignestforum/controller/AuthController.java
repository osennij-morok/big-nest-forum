package net.gastipatis.bignestforum.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.gastipatis.bignestforum.application.exception.EntityValidationException;
import net.gastipatis.bignestforum.application.exception.InvalidCaptchaTokenException;
import net.gastipatis.bignestforum.application.validation.AuthRequestValidator;
import net.gastipatis.bignestforum.gateway.CaptchaGateway;
import net.gastipatis.bignestforum.dto.auth.AuthRequest;
import net.gastipatis.bignestforum.dto.auth.RefreshRequest;
import net.gastipatis.bignestforum.dto.auth.RefreshResponse;
import net.gastipatis.bignestforum.security.JwtManager;
import net.gastipatis.bignestforum.security.exception.InvalidRefreshTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

//@AllArgsConstructor
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
@Tag(name = "Authentication")
@RestController
@RequestMapping("/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtManager jwtManager;
    private final CaptchaGateway captchaGateway;
    private final AuthRequestValidator authRequestValidator;

    @Value("#{new Boolean('${captcha.isRequired}')}")
    private boolean requireCaptcha;

    public AuthController(AuthenticationManager authenticationManager, JwtManager jwtManager, CaptchaGateway captchaGateway, AuthRequestValidator authRequestValidator) {
        this.authenticationManager = authenticationManager;
        this.jwtManager = jwtManager;
        this.captchaGateway = captchaGateway;
        this.authRequestValidator = authRequestValidator;
    }

    @PostMapping("/signIn")
    ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        Errors errors = new BeanPropertyBindingResult(authRequest, "authRequest");
        ValidationUtils.invokeValidator(authRequestValidator, authRequest, errors);
        if (errors.hasErrors()) {
            throw new EntityValidationException(errors);
        }

        if (requireCaptcha) {
            boolean captchaTokenIsInvalid = !captchaGateway.verifyToken(authRequest.getCaptchaToken());
            if (captchaTokenIsInvalid) {
                throw new InvalidCaptchaTokenException();
            }
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()));
        var principal = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok()
                .body(jwtManager.tokensFromUserDetails(principal));
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    RefreshResponse refresh(@RequestBody @Valid RefreshRequest refreshRequest) {
        return new RefreshResponse(jwtManager.refresh(refreshRequest));
    }

    @ExceptionHandler({InvalidRefreshTokenException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    APIError invalidRefreshTokenExceptionHandler(InvalidRefreshTokenException ex) {
        return new APIError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
