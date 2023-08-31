package com.ruanyi.mifish.common.ex;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2020-04-24 20:28
 */
public class BusinessException extends RuntimeException {

    /** errorCode */
    private final ErrorCode errorCode;

    /** throwable */
    private Throwable throwable;

    /**
     * BusinessException
     *
     * @param errorCode
     */
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * BusinessException
     *
     * @param message
     * @param errorCode
     */
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * BusinessException
     *
     * @param errorCode
     * @param throwable
     */
    public BusinessException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
        this.throwable = throwable;
    }

    /**
     * BusinessException
     * 
     * @param errorCode
     * @param message
     * @param cause
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * getErrorCode
     *
     * @return
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * getThrowable
     *
     * @return
     */
    public Throwable getThrowable() {
        return throwable;
    }
}
