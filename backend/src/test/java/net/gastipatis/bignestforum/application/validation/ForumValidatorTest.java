package net.gastipatis.bignestforum.application.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gastipatis.bignestforum.domain.ForumEntity;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.jackson.ProblemModule;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.zalando.problem.Status.BAD_REQUEST;

class ForumValidatorTest {

    private final ForumValidator validator = new ForumValidator();

    @Test
    void given_forumWithSpecialCharsInName_expect_errorWithNameField() {
        ForumEntity forum = ForumEntity.builder()
                .id("f")
                .name("General123//")
                .categoryId(1L)
                .build();
        Errors errors = new BeanPropertyBindingResult(forum, "forum");
        validator.validate(forum, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors();
        assertThat(fieldErrors).isNotEmpty();
        assertThat(fieldErrors)
                .first()
                .extracting(FieldError::getField)
                .isEqualTo("name");
    }

    @Test
    void given_forumWithOnlyAlphanumericChars_expect_noErrors() {
        ForumEntity forum = ForumEntity.builder()
                .id("f")
                .name("General123")
                .categoryId(1L)
                .build();
        Errors errors = new BeanPropertyBindingResult(forum, "forum");
        validator.validate(forum, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors();
        assertThat(fieldErrors).isEmpty();
    }

    @Test
    void sample() throws JsonProcessingException {
        ForumEntity forum = ForumEntity.builder()
                .id("f")
                .name("General123//")
                .categoryId(1L)
                .build();
        Errors errors = new BeanPropertyBindingResult(forum, "forum");
        validator.validate(forum, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors();
        FieldError error = fieldErrors.get(0);
        ThrowableProblem problem = Problem.builder()
                .withTitle("Bad Request")
                .withStatus(BAD_REQUEST)
                .with("code", "FORUM_INCORRECT_NAME_FORMAT")
                .withDetail(error.getDefaultMessage())
                .build();
        var jacksonMapper = new ObjectMapper().registerModule(new ProblemModule().withStackTraces(false));

        String result = jacksonMapper.writeValueAsString(problem);
    }
}