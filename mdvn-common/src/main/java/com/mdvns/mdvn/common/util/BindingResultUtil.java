package com.mdvns.mdvn.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultUtil {

    private static final Logger LOG = LoggerFactory.getLogger(BindingResultUtil.class);

    public static void brResolve(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            LOG.error("请求参数有误：{}", fieldError.getDefaultMessage());
            throw new IllegalArgumentException(fieldError.getDefaultMessage());
        }
    }

}
