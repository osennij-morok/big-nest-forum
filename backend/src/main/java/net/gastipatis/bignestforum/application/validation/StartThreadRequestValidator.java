package net.gastipatis.bignestforum.application.validation;

import net.gastipatis.bignestforum.dto.thread.StartThreadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class StartThreadRequestValidator implements Validator {

    @Value("${thread.op-post.max-length}")
    private int threadOpPostMaxLength;

    @Value("${thread.max-topic-length}")
    private int threadMaxTopicLength;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(StartThreadRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StartThreadRequest request = (StartThreadRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "forumId", "forumId.blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "topic", "topic.blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "text.blank");
        request.getSenderUsername().ifPresent(senderUsername -> {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "senderUsername", "senderUsername.blank");
        });

        if (request.getText().length() > threadOpPostMaxLength) {
            String defaultMessage = String.format(
                    "The thread OP post length is too long. Provided %d, required not longer than %d",
                    request.getText().length(), threadOpPostMaxLength);
            errors.rejectValue("text", "text.tooLong", defaultMessage);
        }
        if (request.getTopic().length() > threadMaxTopicLength) {
            String defaultMessage = String.format(
                    "The thread topic length is too long. Provided %d, required not longer than %d",
                    request.getTopic().length(), threadMaxTopicLength);
            errors.rejectValue("topic", "topic.tooLong", defaultMessage);
        }
    }
}
