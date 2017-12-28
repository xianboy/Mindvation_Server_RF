package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.CustomFunctionLabelRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class RestTemplateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RestTemplateUtil.class);

    /**
     * 获取指定id集合的id和name
     *
     * @param url 查询id和name的url
     * @param staffId 当前用户id
     * @param ids 需要查询的id集合
     * @return list
     */
    public static List<TerseInfo> retrieveTerseInfo(Long staffId, List<Long> ids, String url) throws BusinessException {
        LOG.info("查询terseInfo的url是：【{}】", url);
        //实例化restTemplate对象
        RestTemplate restTemplate = new RestTemplate();
        //构建ParameterizedTypeReference
        ParameterizedTypeReference<RestResponse<TerseInfo[]>> typeRef = new ParameterizedTypeReference<RestResponse<TerseInfo[]>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new RetrieveTerseInfoRequest(staffId, ids));
        //构建responseEntity
        ResponseEntity<RestResponse<TerseInfo[]>> responseEntity = restTemplate.exchange(url,
                HttpMethod.POST, requestEntity, typeRef);
        //获取restResponse
        RestResponse<TerseInfo[]> restResponse = responseEntity.getBody();
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取指定id集合的TerseInfo失败.");
            throw new BusinessException(ErrorEnum.GET_BASE_INFO_FAILED, "获取指定id集合的TerseInfo失败.");
        }
        return Arrays.asList(restResponse.getData());
    }

    /**
     * 自定义过程方法
     *
     * @param customLabelUrl url
     * @param customRequest  request
     * @return label
     * @throws BusinessException exception
     */
    public static Long customLabel(String customLabelUrl, CustomFunctionLabelRequest customRequest) throws BusinessException {
        RestTemplate restTemplate = new RestTemplate();
        //构建ParameterizedTypeReference
        /*ParameterizedTypeReference<RestResponse<FunctionLabelModel>> typeRef = new ParameterizedTypeReference<RestResponse<FunctionLabelModel>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(customRequest);
        //构建responseEntity
        ResponseEntity<RestResponse<FunctionLabelModel>> responseEntity = restTemplate.exchange(customLabelUrl,
                HttpMethod.POST, requestEntity, typeRef);
        //获取restResponse
        RestResponse<FunctionLabelModel> restResponse = responseEntity.getBody();
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取指定id集合的id和name失败.");
            throw new BusinessException(ErrorEnum.CUSTOM_LABEL_FAILD, "获取指定id集合的id和name失败.");
        }*/
        Long id = restTemplate.postForObject(customLabelUrl, customRequest, Long.class);
//        FunctionLabelModel labelModel = restResponse.getData();
        return id;
    }


}
