package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.CustomFunctionLabelRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.Delivery;
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
        Long id = restTemplate.postForObject(customLabelUrl, customRequest, Long.class);
        return id;
    }

    /**
     * 处理过程方法: 如果FunctionLabel 为Long,返回; 如果为字符串, 则自定义
     * @param customLabelUrl customLabelUrl
     * @param creatorId creatorId
     * @param hostSerialNo hostSerialNo
     * @param functionLabel functionLabel
     * @return Long
     * @throws BusinessException BusinessException
     */
    public static Long buildLabel(String customLabelUrl, Long creatorId, String hostSerialNo, Object functionLabel) throws BusinessException {
        Long id = null;
        //如果functionLabel为Long类型, 则为已存在的过程方法的id,直接返回
        if (functionLabel instanceof Integer) {
            id = Long.valueOf((Integer) functionLabel);
        } else if (functionLabel instanceof String) {
            try {
                id = Long.valueOf(functionLabel.toString());
            } catch (Exception ex) {
                //自定义过程方法
                id = RestTemplateUtil.customLabel(customLabelUrl, new CustomFunctionLabelRequest(creatorId, hostSerialNo, (String) functionLabel));
            }
        }
        return id;
    }

    /**
     * 根据模板id获取模板对应的所有角色
     * @param staffId staffId
     * @param templateId templateId
     * @param retrieveRolesUrl retrieveRolesUrl
     * @return List
     * @throws BusinessException exception
     */
    public static List<TerseInfo> getTemplateRoles(Long staffId, Long templateId, String retrieveRolesUrl) throws BusinessException {
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<RestResponse<TerseInfo[]>> typeRef = new ParameterizedTypeReference<RestResponse<TerseInfo[]>>() {
        };
        HttpEntity<?> requestEntity = new HttpEntity(new SingleCriterionRequest(staffId, templateId.toString()));
        ResponseEntity<RestResponse<TerseInfo[]>> responseEntity = restTemplate.exchange(retrieveRolesUrl, HttpMethod.POST, requestEntity, typeRef, new Object[0]);
        RestResponse<TerseInfo[]> restResponse = responseEntity.getBody();
        if (!"000".equals(restResponse.getCode())) {
            LOG.error("获取指定id集合的TerseInfo失败.");
            throw new BusinessException(ErrorEnum.GET_BASE_INFO_FAILED, "获取指定id集合的TerseInfo失败.");
        } else {
            return Arrays.asList(restResponse.getData());
        }
    }

    /**
     * 获取指定id的交付件
     * @param url url
     * @param retrieveRequest request
     * @return Delivery
     * @throws BusinessException BusinessException
     */
    public static Delivery getDeliveryById(String url, SingleCriterionRequest retrieveRequest) throws BusinessException {
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<RestResponse<Delivery>> typeRef = new ParameterizedTypeReference<RestResponse<Delivery>>() {
        };
        ResponseEntity<RestResponse<Delivery>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Object>(retrieveRequest), typeRef, RestResponse.class);
        RestResponse<Delivery> restResponse = responseEntity.getBody();
        if (!"000".equals(restResponse.getCode())) {
            LOG.error("获取ID为【】的交付件失败.", retrieveRequest.getCriterion());
            throw new BusinessException(ErrorEnum.DELIVERY_NOT_EXIST, "获取id为【"+retrieveRequest.getCriterion()+"】的交付件失败.");
        } else {
            return restResponse.getData();
        }

    }
}
