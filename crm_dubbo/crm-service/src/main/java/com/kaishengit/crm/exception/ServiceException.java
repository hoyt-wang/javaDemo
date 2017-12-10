package com.kaishengit.crm.exception;

/**
 * Created by hoyt on 2017/11/8.
 */

public class ServiceException extends RuntimeException{

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable th) {
        super(th);
    }

    public ServiceException(Throwable th,String message) {
        super(message,th);
    }
}
