package com.flab.livecommerce.domain.user.exception;

import com.flab.livecommerce.common.exception.BaseException;
import com.flab.livecommerce.common.response.ErrorCode;

public class InvalidUserException extends BaseException {


    public InvalidUserException() {
        super(ErrorCode.INVALID_USER);
    }

    public InvalidUserException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
