package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.SendMessageRequest;
import com.mdvns.mdvn.common.bean.model.ServerPush;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.List;

public class ServerPushUtil {


    /**
     * 消息推送
     * @param initiatorId 发起者Id(也是登录者Id)
     * @param serialNo 所属编号
     * @param type 消息推送的类型
     * @param staffIds 接收消息的list
     * @return
     * @throws BusinessException
     */
    public static Boolean serverPush(Long initiatorId, String serialNo,String subjectType, String type, List<Long> staffIds) throws BusinessException {
        try {
            //实例化restTem对象
            RestTemplate restTemplate = new RestTemplate();
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            ServerPush serverPush = new ServerPush();
            /**
             * 查询发起者详细信息
             */
            String retrieveByIdUrl = "http://localhost:20001/mdvn-staff/staff/retrieveById";
            SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
            singleCriterionRequest.setCriterion(String.valueOf(initiatorId));
            singleCriterionRequest.setStaffId(initiatorId);

//            //构建ParameterizedTypeReference
//            ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<RestResponse<Staff>>() {
//            };
//            //构建requestEntity
//            HttpEntity<?> requestEntity = new HttpEntity<>(new RetrieveTerseInfoRequest(staffId, ids));
//            //构建responseEntity
////            Staff initiator = (Staff) FetchListUtil.fetch(restTemplate,retrieveByIdUrl,singleCriterionRequest, parameterizedTypeReference);
//            //获取restResponse
//            RestResponse<TerseInfo[]> restResponse = responseEntity.getBody();
//            RestResponse<Staff> restResponse = restTemplate.postForObject(retrieveByIdUrl, singleCriterionRequest, RestResponse.class);
//            Staff initiator = restResponse.getData();
//            serverPush.setInitiator(initiator);
            /**
             * 区分是消息更改的位置（project、requirement、story、task、comment、issue等）
             */
            serverPush.setSubjectId(serialNo);
            serverPush.setSubjectType(subjectType);
            /**
             * 区分消息更改的类型（create、update、at等）
             */
            serverPush.setType(type);
            sendMessageRequest.setInitiatorId(initiatorId);
            sendMessageRequest.setStaffIds(staffIds);
            sendMessageRequest.setServerPushResponse(serverPush);
            String sendMessageUrl = "http://localhost:10027/mdvn-websocket/websocket/sendMessage";
            Boolean flag = restTemplate.postForObject(sendMessageUrl, sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.SERVER_PUSH_FAILD, "消息推送" + type + serialNo + "失败");
        }
        return true;
    }


}
