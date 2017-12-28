package com.mdvns.mdvn.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends Throwable {

    private String code;

    private String msg;


    /**
     * 使用errorEnum 构建BusibessException
     * @param errorEnum
     */
    public BusinessException(ErrorEnum errorEnum, String msg) {
        this.code = errorEnum.getCode();
        this.msg = msg;
    }

}
