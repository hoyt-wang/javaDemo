package com.kaishengit.weixin.exception;

/**
 * Created by hoyt on 2017/11/20.
 */

public class WeixinException extends RuntimeException {

    public WeixinException() {
    }
    public WeixinException(String message) {
        super(message);
    }
    public WeixinException(Throwable throwable) {
        super(throwable);
    }

    public WeixinException(String message,Throwable throwable) {
        super(message,throwable);
    }
}
