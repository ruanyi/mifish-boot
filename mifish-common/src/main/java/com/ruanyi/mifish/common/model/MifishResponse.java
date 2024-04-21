package com.ruanyi.mifish.common.model;

import com.ruanyi.mifish.common.ex.MifishErrorCode;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2024-04-21 14:47
 */
@Getter
@Setter
public class MifishResponse {

    /** success */
    private boolean success;

    /** errorCode */
    private String errorCode;

    /** errorMessage */
    private String errorMessage;

    /** data */
    private Object data;

    /**
     * SUCCESS
     * 
     * @param data
     * @return
     * @param <T>
     */
    public static <T> MifishResponse SUCCESS(T data) {
        MifishResponse mifishResponse = new MifishResponse();
        mifishResponse.setSuccess(true);
        mifishResponse.setErrorCode(MifishErrorCode.SUCCESS.getCode());
        mifishResponse.setErrorMessage(MifishErrorCode.SUCCESS.getMessage());
        mifishResponse.setData(data);
        return mifishResponse;
    }

    /**
     * SUCCESS
     * 
     * @param message
     * @param data
     * @return
     * @param <T>
     */
    public static <T> MifishResponse SUCCESS(String message, T data) {
        MifishResponse mifishResponse = new MifishResponse();
        mifishResponse.setSuccess(true);
        mifishResponse.setErrorCode(MifishErrorCode.SUCCESS.getCode());
        mifishResponse.setErrorMessage(message);
        mifishResponse.setData(data);
        return mifishResponse;
    }

    /**
     * FAILURE
     * 
     * @param errorCode
     * @return
     */
    public static MifishResponse FAILURE(MifishErrorCode errorCode) {
        MifishResponse singleResponse = new MifishResponse();
        singleResponse.setSuccess(false);
        singleResponse.setErrorCode(errorCode.getCode());
        singleResponse.setErrorMessage(errorCode.getMessage());
        return singleResponse;
    }

    /**
     * FAILURE
     *
     * @param message
     * @param errorCode
     * @return
     */
    public static MifishResponse FAILURE(String message, MifishErrorCode errorCode) {
        MifishResponse singleResponse = new MifishResponse();
        singleResponse.setSuccess(false);
        singleResponse.setErrorCode(errorCode.getCode());
        singleResponse.setErrorMessage(message);
        return singleResponse;
    }

}
