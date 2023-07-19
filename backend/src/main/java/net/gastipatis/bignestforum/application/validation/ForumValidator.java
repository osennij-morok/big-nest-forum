package net.gastipatis.bignestforum.application.validation;

import net.gastipatis.bignestforum.domain.ForumEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ForumValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ForumEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.blank");
        ForumEntity forum = (ForumEntity) target;
        boolean forumNameContainsUnsupportedChar = forum.getName()
                .chars()
                .anyMatch(ch -> !Character.isAlphabetic(ch) && !Character.isDigit(ch) || ch >= 128);
        if (forumNameContainsUnsupportedChar) {
            errors.rejectValue("name", "invalid.forumName.format",
                    "Incorrect name format: it must contain only alphanumeric characters");
        }
    }
}
