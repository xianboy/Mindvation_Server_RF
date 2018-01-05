package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.List;

public class ServerPushUtil {

    /*实例化LOG常量*/
    private static final Logger LOG = LoggerFactory.getLogger(ServerPushUtil.class);
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
            ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
            };
            ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(retrieveByIdUrl, HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
            RestResponse<Staff> restResponse = responseEntity.getBody();
            serverPush.setInitiator(restResponse.getData());
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
            String sendMessageUrl = "http://localhost:20009/mdvn-websocket/websocket/sendMessage";
            Boolean flag = restTemplate.postForObject(sendMessageUrl, sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            LOG.error("消息推送" + type + serialNo + "失败;"+"异常信息：" + e);
            //消息推送不能影响其他service的运行
//            throw new BusinessException(ErrorEnum.SERVER_PUSH_FAILD, "消息推送" + type + serialNo + "失败");
        }
        return true;
    }

    /**
     * 消息推送(关于task)
     * @param initiatorId 发起者Id(也是登录者Id)
     * @param serialNo 所属编号
     * @param type 消息推送的类型
     * @param staffIds 接收消息的list
     * @return
     * @throws BusinessException
     */
    public static Boolean serverPushByTask(Long initiatorId, String serialNo,String subjectType, String type, List<Long> staffIds,String taskByStoryId) throws BusinessException {
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
            ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
            };
            ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(retrieveByIdUrl, HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
            RestResponse<Staff> restResponse = responseEntity.getBody();
            serverPush.setInitiator(restResponse.getData());
            /**
             * 区分是消息更改的位置（project、requirement、story、task、comment、issue等）
             */
            serverPush.setSubjectId(serialNo);
            serverPush.setSubjectType(subjectType);
            /**
             * 区分消息更改的类型（create、update、at等）
             */
            serverPush.setType(type);
            /**
             * task返回storyId
             */
            serverPush.setTaskByStoryId(taskByStoryId);
            sendMessageRequest.setInitiatorId(initiatorId);
            sendMessageRequest.setStaffIds(staffIds);
            sendMessageRequest.setServerPushResponse(serverPush);
            String sendMessageUrl = "http://localhost:20009/mdvn-websocket/websocket/sendMessage";
            Boolean flag = restTemplate.postForObject(sendMessageUrl, sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            LOG.error("消息推送" + type + serialNo + "失败;"+"异常信息：" + e);
            //消息推送不能影响其他service的运行
//            throw new BusinessException(ErrorEnum.SERVER_PUSH_FAILD, "消息推送" + type + serialNo + "失败");
        }
        return true;
    }

    /**
     * 消息推送(关于task进度)
     * @param initiatorId 发起者Id(也是登录者Id)
     * @param serialNo 所属编号
     * @param type 消息推送的类型
     * @param staffIds 接收消息的list
     * @return
     * @throws BusinessException
     */
    public static Boolean serverPushByTaskProgress(Long initiatorId, String serialNo,String subjectType, String type, List<Long> staffIds,String taskByStoryId,Integer newProgress,Integer oldProgress) throws BusinessException {
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
            ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
            };
            ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(retrieveByIdUrl, HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
            RestResponse<Staff> restResponse = responseEntity.getBody();
            serverPush.setInitiator(restResponse.getData());
            /**
             * 区分是消息更改的位置（project、requirement、story、task、comment、issue等）
             */
            serverPush.setSubjectId(serialNo);
            serverPush.setSubjectType(subjectType);
            /**
             * 区分消息更改的类型（create、update、at等）
             */
            serverPush.setType(type);
            /**
             * task返回storyId和进度
             */
            serverPush.setTaskByStoryId(taskByStoryId);
            serverPush.setNewProgress(newProgress);
            serverPush.setOldProgress(oldProgress);
            sendMessageRequest.setInitiatorId(initiatorId);
            sendMessageRequest.setStaffIds(staffIds);
            sendMessageRequest.setServerPushResponse(serverPush);
            String sendMessageUrl = "http://localhost:20009/mdvn-websocket/websocket/sendMessage";
            Boolean flag = restTemplate.postForObject(sendMessageUrl, sendMessageRequest, Boolean.class);
            System.out.println(flag);
        } catch (Exception e) {
            LOG.error("消息推送" + type + serialNo + "失败;"+"异常信息：" + e);
            //消息推送不能影响其他service的运行
//            throw new BusinessException(ErrorEnum.SERVER_PUSH_FAILD, "消息推送" + type + serialNo + "失败");
        }
        return true;
    }


}
