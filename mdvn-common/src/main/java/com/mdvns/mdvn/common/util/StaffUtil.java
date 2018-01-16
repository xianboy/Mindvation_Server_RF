package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.Staff;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StaffUtil {

    /**
     * 通过staffId获取staff详情
     *
     * @param id
     * @return
     */
    public static Staff rtrvStaffInfoById(Long id,String rtrvStaffInfoByIdUrl) {
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
        singleCriterionRequest.setCriterion(String.valueOf(id));
        singleCriterionRequest.setStaffId(id);
        ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
        };
        ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(rtrvStaffInfoByIdUrl, HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
        RestResponse<Staff> restResponse = responseEntity.getBody();
        return restResponse.getData();
    }
}
