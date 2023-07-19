package net.gastipatis.bignestforum.application.validation;

import net.gastipatis.bignestforum.dto.forum.CreateForumRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CreateForumRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateForumRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateForumRequest request = (CreateForumRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "id", "id.blank", "Forum ID cannot be blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "name", "name.blank", "Forum name cannot be blank");

        if (request.getCategoryId() <= 0) {
            String defaultMessage = "Forum category id must be more than zero";
            errors.rejectValue("categoryId", "categoryId.invalid", defaultMessage);
        }
    }
}
