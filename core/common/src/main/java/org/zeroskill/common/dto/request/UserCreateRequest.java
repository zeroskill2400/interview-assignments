package org.zeroskill.common.dto.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroskill.common.dto.UserDto;
import org.zeroskill.common.entity.Gender;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.validation.Check;

import static org.zeroskill.common.util.Util.isValidEmail;
import static org.zeroskill.common.util.Util.isValidEnum;

public record UserCreateRequest(
        String username,
        String password,
        String email,
        Integer age,
        String phone,
        Gender gender
) implements Check {
    private static final Logger logger = LogManager.getLogger(UserCreateRequest.class);

    public static UserDto toUserDto(UserCreateRequest request) {
        return new UserDto(null, request.username(), request.email, request.password, request.age, request.phone(), request.gender, null, null);
    }

    @Override
    public boolean check() {
        if (username == null || username.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }
        if (password == null || password.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        if (email == null || email.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        if (age == null || age < 0) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        if (phone == null || phone.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        if (gender == null) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        if (!isValidEnum(Gender.class, gender.toString())) {
            throw new InterviewException(ErrorType.INVALID_FIELD_VALUE, logger::error);
        }

        if (!isValidEmail(email)) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        return true;
    }
}
