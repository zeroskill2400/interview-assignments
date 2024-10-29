package org.zeroskill.common.dto.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroskill.common.entity.Gender;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.validation.Check;

import static org.zeroskill.common.util.Util.isValidEmail;
import static org.zeroskill.common.util.Util.isValidEnum;

public record UserUpdateRequest(
        String username,
        String password,
        String email,
        Integer age,
        String phone,
        Gender gender
) implements Check {
    private static final Logger logger = LogManager.getLogger(UserUpdateRequest.class);

    @Override
    public boolean check() {
        if (!isValidEmail(email)) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        if (gender != null && !isValidEnum(Gender.class, gender.toString())) {
            throw new InterviewException(ErrorType.INVALID_FIELD_VALUE, logger::error);
        }

        return true;
    }
}
