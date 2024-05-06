package com.ruanyi.mifish.web.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.common.ex.MifishErrorCode;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.model.MifishResponse;
import com.ruanyi.mifish.common.model.OperateResult;

/**
 * 框架异常统一处理
 *
 * @author smartlv
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** log */
    private static final MifishLogs LOG = MifishLogs.framework;

    /**
     * 统一处理参数异常：例如：绑定异常，参数不存在，参数类型不合法，没有找到handler异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {BindException.class, MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class})
    public MifishResponse handleBindException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        // other case
        LOG.error(ex, Pair.of("clazz", "GlobalExceptionHandler"), Pair.of("method", "handleBindException"),
            Pair.of("action", request.getRequestURI()), Pair.of("query_string", request.getQueryString()),
            Pair.of("status", "exception"), Pair.of("http_status", response.getStatus()));
        if (ex instanceof BindException) {
            return buildCommonParamCheckResp((BindException)ex, request);
        } else if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException missEx = (MissingServletRequestParameterException)ex;
            return MifishResponse.FAILURE("Miss parameter(" + missEx.getParameterName() + ")",
                MifishErrorCode.ILLEGAL_ARGUMENT);
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException matmex = (MethodArgumentTypeMismatchException)ex;
            return MifishResponse.FAILURE("Parameter(" + matmex.getName() + ") invalid",
                MifishErrorCode.ILLEGAL_ARGUMENT);
        } else {
            return MifishResponse.FAILURE("system error", MifishErrorCode.UNKNOW_EXCEPTION);
        }
    }

    /**
     * 统一处理参数异常：没有找到handler异常，返回httpcode:404异常
     * <p>
     * 谨防第三方刷接口，导致大量的错误日志
     *
     * @param nfex
     * @param request
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<OperateResult> handleNoHandlerFoundException(NoHandlerFoundException nfex,
        HttpServletRequest request) {
        // other case
        LOG.warn(nfex, Pair.of("clazz", "GlobalExceptionHandler"), Pair.of("method", "handleNoHandlerFoundException"),
            Pair.of("action", request.getRequestURI()), Pair.of("query_string", request.getQueryString()),
            Pair.of("status", "exception"), Pair.of("http_status", "404"));
        return new ResponseEntity(OperateResult.FAILURE("Invalid request", "404"), HttpStatus.NOT_FOUND);
    }

    /**
     * 统一处理客户端终止异常
     *
     * @param cax 异常
     * @return
     */
    @ExceptionHandler(value = {ClientAbortException.class})
    @ResponseBody
    public MifishResponse handleClientAbortException(ClientAbortException cax, HttpServletRequest request,
        HttpServletResponse response) {
        // 返回499
        response.setStatus(499);
        // log debug message,it is a normal exception
        if (LOG.isDebugEnabled()) {
            LOG.debug(Pair.of("clazz", "GlobalExceptionHandler"), Pair.of("method", "handleClientAbortException"),
                Pair.of("action", request.getRequestURI()), Pair.of("query_string", request.getQueryString()),
                Pair.of("status", "exception"), Pair.of("http_status", response.getStatus()));
        }
        return MifishResponse.FAILURE("system error", MifishErrorCode.UNKNOW_EXCEPTION);
    }

    /**
     * handleBusinessException
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(value = {BusinessException.class})
    @ResponseBody
    public MifishResponse handleBusinessException(BusinessException ex, HttpServletRequest request,
        HttpServletResponse response) {
        // business exception
        MifishErrorCode mifishErrorCode =
            (ex.getErrorCode() != null ? ex.getErrorCode() : MifishErrorCode.UNKNOW_EXCEPTION);
        LOG.error(ex, Pair.of("clazz", "GlobalExceptionHandler"), Pair.of("method", "BusinessException"),
            Pair.of("action", request.getRequestURI()), Pair.of("query_string", request.getQueryString()),
            Pair.of("http_status", response.getStatus()), Pair.of("errorCode", mifishErrorCode.getCode()));
        return MifishResponse.FAILURE(ex.getMessage(), ex.getErrorCode());
    }

    /**
     * 统一处理其他异常
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public MifishResponse handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        // other case
        LOG.error(ex, Pair.of("clazz", "GlobalExceptionHandler"), Pair.of("method", "handleException"),
            Pair.of("action", request.getRequestURI()), Pair.of("query_string", request.getQueryString()),
            Pair.of("status", "exception"), Pair.of("http_status", response.getStatus()));
        return MifishResponse.FAILURE("system error", MifishErrorCode.UNKNOW_EXCEPTION);
    }

    /**
     * buildCommonParamCheckResp
     * <p>
     * 
     * @param bex
     * @param request
     * @return
     */
    private MifishResponse buildCommonParamCheckResp(BindException bex, HttpServletRequest request) {
        BindingResult br = bex.getBindingResult();
        FieldError fieldError = br.getFieldError();
        Object fieldValue = fieldError.getRejectedValue();
        if (fieldValue == null) {
            return MifishResponse.FAILURE("Miss parameter(" + fieldError.getField() + ")",
                MifishErrorCode.ILLEGAL_ARGUMENT);
        } else {
            return MifishResponse.FAILURE("Parameter(" + fieldError.getField() + ") invalid",
                MifishErrorCode.ILLEGAL_ARGUMENT);
        }
    }
}
