package com.ruanyi.mifish.common.ex;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-09-13 21:30
 */
public class MissingRequestParameterException extends RuntimeException {

    /** parameterName */
    private final String parameterName;

    /** parameterType */
    private final String parameterType;

    /**
     * Constructor for MissingRequestParameterException.
     *
     * @param parameterName the name of the missing parameter
     * @param parameterType the expected type of the missing parameter
     */
    public MissingRequestParameterException(String parameterName, String parameterType) {
        super("");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    /**
     * Constructor for MissingRequestParameterException.
     *
     * @param parameterName the name of the missing parameter
     * @param parameterType the expected type of the missing parameter
     * @param cause
     */
    public MissingRequestParameterException(String parameterName, String parameterType, Throwable cause) {
        super(cause);
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    /**
     * getMessage
     *
     * @return
     */
    @Override
    public String getMessage() {
        return "Required " + this.parameterType + " parameter '" + this.parameterName + "' is not present";
    }

    /**
     * Return the name of the offending parameter.
     */
    public final String getParameterName() {
        return this.parameterName;
    }

    /**
     * Return the expected type of the offending parameter.
     */
    public final String getParameterType() {
        return this.parameterType;
    }
}
