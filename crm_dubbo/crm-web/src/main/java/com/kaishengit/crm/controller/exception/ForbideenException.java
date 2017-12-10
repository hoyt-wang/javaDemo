package com.kaishengit.crm.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by hoyt on 2017/11/10.
 */

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbideenException extends RuntimeException {

    public ForbideenException() {
    }

    public ForbideenException(String message) {
        super(message);
    }
}
