package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.RoleMember;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.exception.BusinessException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取一个story或者reqmnt下所有不重复的人员Id(知道角色成员list的情况下)
     * @param roleMembers
     * @return
     * @throws BusinessException
     */
    public static List<Long> getDistinctMembers(List<RoleMember> roleMembers) throws BusinessException {
        List<Long> memberIds = new ArrayList<>();
        for (int i = 0; i < roleMembers.size(); i++) {
            List<TerseInfo> members = roleMembers.get(i).getMembers();
            if (!StringUtils.isEmpty(members)) {
                for (int j = 0; j < members.size(); j++) {
                    Long memberId = members.get(j).getId();
                    if (!memberIds.isEmpty() && memberIds.contains(memberId)) {
                        continue;
                    }
                    memberIds.add(memberId);
                }
            }
        }
        return memberIds;
    }
}