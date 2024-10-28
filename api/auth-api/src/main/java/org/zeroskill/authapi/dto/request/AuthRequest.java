package org.zeroskill.authapi.dto.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.validation.Check;

public record AuthRequest(
        String userName,
        String password
) implements Check {
    private static final Logger logger = LogManager.getLogger(AuthRequest.class);

    @Override
    public boolean check() {
        if (userName == null || userName.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        if (password == null || password.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }

        return true;
    }
}
