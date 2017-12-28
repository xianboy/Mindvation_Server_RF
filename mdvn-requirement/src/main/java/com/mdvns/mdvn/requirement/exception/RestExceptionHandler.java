package com.mdvns.mdvn.requirement.exception;


import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    //SAPI调用异常
//    @ExceptionHandler(BusinessException.class)
    @ExceptionHandler(BusinessException.class)
    public RestResponse<?> businessExceptionHandler(BusinessException ex) {
        LOG.error("Internal Server Error:{}", ex.getMessage());
        return RestResponseUtil.error(ex.getCode(), ex.getLocalizedMessage());
    }

    //请求参数不正确：
    @ExceptionHandler(IllegalArgumentException.class)
    public RestResponse<?> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        LOG.error("请求参数不正确:{}", ex.getLocalizedMessage());
        return RestResponseUtil.error(ErrorEnum.ILLEGAL_ARG.getCode(), ex.getLocalizedMessage());

    }

}