package org.zeroskill.common.dto.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.validation.Check;

public record AddPostRequest(
    String title,
    String content
) implements Check {
    private static final Logger logger = LogManager.getLogger(AddPostRequest.class);

    @Override
    public boolean check() {
        if (title == null || title.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }
        if (content == null || content.isEmpty()) {
            throw new InterviewException(ErrorType.EMPTY_FIELD, logger::error);
        }
        return true;
    }
}
