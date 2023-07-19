package net.gastipatis.bignestforum.application.validation;

import net.gastipatis.bignestforum.dto.auth.SignUpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SignUpRequestValidator implements Validator {

    @Value("#{new Boolean('${captcha.isRequired}')}")
    private boolean captchaIsRequired;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.blank");
        if (captchaIsRequired) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "captchaToken", "captchaToken.blank");
        }
    }
}
