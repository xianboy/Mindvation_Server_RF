package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class RestResponseUtil {


    /**
     * 构建restfulResponse
     * @param data
     * @return
     */
    public static RestResponse<?> success(Object data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode("000");
        restResponse.setMsg("SUCCESS");
        if (null != data) {
            restResponse.setData(data);
        }
        return restResponse;
    }

    public static ResponseEntity<?> successResponseEntity(Object object) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode("000");
        restResponse.setMsg("SUCCESS");
        if (null != object) {
            restResponse.setData(object);
        }
        return ResponseEntity.ok(restResponse);
    }

    public static ResponseEntity<?> successResponseEntity() {

        return successResponseEntity(null);
    }

    /**
     * 构建异常response
     * @param code
     * @param msg
     * @return
     */
    public static RestResponse<?> error(String code, String msg) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(code);
        restResponse.setMsg(msg);

        return restResponse;
    }

    /**
     * 构建异常response
     * @param be
     * @return
     */
    public static RestResponse<?> error(BusinessException be) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(be.getCode());
        restResponse.setMsg(be.getMsg());
        return restResponse;
    }


}
