package net.gastipatis.bignestforum.application.validation;

import net.gastipatis.bignestforum.dto.post.PostInThreadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PostInThreadRequestValidator implements Validator {

    @Value("${thread.post.max-length}")
    private int postMaxLength;

    @Value("${thread.max-topic-length}")
    private int postMaxTopicLength;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PostInThreadRequest.class);
    }

    // TODO: remove code duplication
    @Override
    public void validate(Object target, Errors errors) {
        PostInThreadRequest request = (PostInThreadRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "forumId", "forumId.blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "text.blank");
        request.getSenderUsername().ifPresent(senderUsername -> {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "senderUsername", "senderUsername.blank");
        });

        if (request.getThreadHeadPostId() <= 0) {
            String defaultMessage = "The thread head post id must be more than zero";
            errors.rejectValue("threadHeadPostId", "threadHeadPostId.invalid", defaultMessage);
        }
        if (request.getText().length() > postMaxLength) {
            String defaultMessage = String.format(
                    "The post text length is too long. Provided %d, required not longer than %d",
                    request.getText().length(), postMaxLength);
            errors.rejectValue("text", "text.tooLong", defaultMessage);
        }
        if (request.getTopic().length() > postMaxTopicLength) {
            String defaultMessage = String.format(
                    "The post topic length is too long. Provided %d, required not longer than %d",
                    request.getTopic().length(), postMaxTopicLength);
            errors.rejectValue("topic", "topic.tooLong", defaultMessage);
        }
    }
}
