package com.ruanyi.mifish.common.ex;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-04-24 20:28
 */
public class BusinessException extends RuntimeException {

    /** errorCode */
    private final MifishErrorCode mifishErrorCode;

    /** throwable */
    private Throwable throwable;

    /**
     * BusinessException
     *
     * @param mifishErrorCode
     */
    public BusinessException(MifishErrorCode mifishErrorCode) {
        this.mifishErrorCode = mifishErrorCode;
    }

    /**
     * BusinessException
     *
     * @param message
     * @param mifishErrorCode
     */
    public BusinessException(String message, MifishErrorCode mifishErrorCode) {
        super(message);
        this.mifishErrorCode = mifishErrorCode;
    }

    /**
     * BusinessException
     *
     * @param mifishErrorCode
     * @param throwable
     */
    public BusinessException(MifishErrorCode mifishErrorCode, Throwable throwable) {
        super(throwable);
        this.mifishErrorCode = mifishErrorCode;
        this.throwable = throwable;
    }

    /**
     * BusinessException
     * 
     * @param mifishErrorCode
     * @param message
     * @param cause
     */
    public BusinessException(MifishErrorCode mifishErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.mifishErrorCode = mifishErrorCode;
    }

    /**
     * getErrorCode
     *
     * @return
     */
    public MifishErrorCode getErrorCode() {
        return mifishErrorCode;
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
