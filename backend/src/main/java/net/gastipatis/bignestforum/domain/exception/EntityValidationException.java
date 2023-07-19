package net.gastipatis.bignestforum.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Deprecated
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class EntityValidationException extends RuntimeException {

    public EntityValidationException(String field, String description) {
        super(String.format("Incorrect field [%s] value. %s", field, description));
    }

    public static EntityValidationException blankField(String field) {
        String description = field + " cannot be blank";
        return new EntityValidationException(field, description);
    }

    public static EntityValidationException tooShortField(String field, int actualLength, int requiredLength) {
        String description = String.format(
                "%s is too short. Expected length is %d. Actual length is %d",
                field, requiredLength, actualLength);
        return new EntityValidationException(field, description);
    }

    public static EntityValidationException tooLongField(String field, int actualLength, int requiredLength) {
        String description = String.format(
                "%s is too long. Expected length is %d. Actual length is %d",
                field, requiredLength, actualLength);
        return new EntityValidationException(field, description);
    }

    public static EntityValidationException nullField(String field) {
        String description = field + " cannot be null";
        return new EntityValidationException(field, description);
    }
}
